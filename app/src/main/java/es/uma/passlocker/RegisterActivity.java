package es.uma.passlocker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.uma.passlocker.db.dao.UserDao;
import es.uma.passlocker.db.entities.UserEntity;
import es.uma.passlocker.keyStore.EncryptionHelper;
import es.uma.passlocker.keyStore.HashHelper;
import es.uma.passlocker.keyStore.KeyStoreHelper;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.main_layout).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        try {
            KeyStoreHelper.getKey();
        } catch (Exception e) {
            e.printStackTrace();
            String okButton = getString(R.string.okButton);
            String errorMesage = getString(R.string.keyStoreError);
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(errorMesage)
                    .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                    .show();
            return;
        }

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            try {
                if (!this.checkCredentials(email, password, confirmPassword)) {
                    return;
                }
                // Cifrar la contraseña
                // byte[] encryptedPassword = EncryptionHelper.encrypt(password);
                byte[] encryptedPassword = password.getBytes();

                // Generar el hash de la contraseña cifrada
                String hashedPassword = HashHelper.generateHash(encryptedPassword);

                int userId = this.saveUser(email, hashedPassword);
                String success = getString(R.string.registerSuccess);
                Toast.makeText(this, success, Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("loggedInUserId", userId);
                editor.apply();

                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                String okButton = getString(R.string.okButton);
                String errorMesage = getString(R.string.passwordRegistration);
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(errorMesage)
                        .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                        .show();
                return;
            }
        });
    }

    // Guardar los datos en la base de datos (implementa según tu lógica)
    private boolean checkCredentials(String email, String password, String confirmPassword) {
        UserDao userDao = UserDao.getInstance();
        UserEntity user = userDao.getUser(email);
        if (user != null) {
            String message = getString(R.string.registerUserException);
            String okButton = getString(R.string.okButton);
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                    .show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            String message = getString(R.string.registerPasswordException);
            String okButton = getString(R.string.okButton);
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(message)
                    .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                    .show();            return false;
        }

        return true;
    }

    private int saveUser(String email, String password) {
        UserDao userDao = UserDao.getInstance();
        userDao.insertUser(email, password);
        int id = userDao.getUser(email).getId();
        return id;
    }
}

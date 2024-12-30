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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.main_layout).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

         try {
            KeyStoreHelper.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al inicializar KeyStore", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            String errorMesage = getString(R.string.signInException);
            String okButton = getString(R.string.okButton);
            try {
                byte[] encryptedPassword = password.getBytes();

                // Generar el hash de la contraseña
                String hashedPassword = HashHelper.generateHash(encryptedPassword);

                if(checkPassword(email, hashedPassword)) {
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                    int userId = getUserId(email);

                    SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("loggedInUserId", userId);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("Error")
                            .setMessage(errorMesage)
                            .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                            .show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(errorMesage)
                        .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });
    }

    // Guardar los datos en la base de datos (implementa según tu lógica)
    private boolean checkPassword(String email, String hashedPassword) {
        UserDao userDao = UserDao.getInstance();
        UserEntity user = userDao.getUser(email);
        return hashedPassword.equals(user.getPassword());
    }

    private int getUserId(String email) {
        UserDao userDao = UserDao.getInstance();
        UserEntity user = userDao.getUser(email);
        return user.getId();
    }
}

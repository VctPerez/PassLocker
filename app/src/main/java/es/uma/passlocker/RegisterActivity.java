package es.uma.passlocker;

import android.content.Intent;
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

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        try {
            KeyStoreHelper.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al inicializar KeyStore", Toast.LENGTH_SHORT).show();
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

                this.saveUser(email, hashedPassword);

                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MenuActivity.class);
                startActivity(intent);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al procesar la contraseña", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Guardar los datos en la base de datos (implementa según tu lógica)
    private boolean checkCredentials(String email, String password, String confirmPassword) {
        UserDao userDao = UserDao.getInstance();
        UserEntity user = userDao.getUser(email);
        if (user != null) {
            Toast.makeText(this, "Usuario ya registrado", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas deben ser iguales", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveUser(String email, String password) {
        System.out.println(email);
        System.out.println(password);
        UserDao userDao = UserDao.getInstance();
        userDao.insertUser(email, password);
        System.out.println(userDao.getUser(email));
    }
}

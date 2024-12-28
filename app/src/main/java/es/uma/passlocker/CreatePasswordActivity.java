package es.uma.passlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Base64;

import es.uma.passlocker.db.dao.PasswordInfoDao;
import es.uma.passlocker.keyStore.EncryptionHelper;
import es.uma.passlocker.utils.PasswordGenerator;

public class CreatePasswordActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = getIntent().getIntExtra("id", 0);
        String instanceTitle = getIntent().getStringExtra("site_name");
        String instancePassword = getIntent().getStringExtra("password");
        String instanceUrl = getIntent().getStringExtra("site_url");
        String instanceNotes = getIntent().getStringExtra("notes");

        setContentView(R.layout.activity_create_password);

        Button buttonTogglePassword = findViewById(R.id.buttonTogglePassword);
        EditText etPassword = findViewById(R.id.editTextPassword);
        EditText etTitle = findViewById(R.id.editTextTitle);
        EditText etUrl = findViewById(R.id.editTextURL);
        EditText etNotes = findViewById(R.id.editTextNotes);

        etPassword.setText(instancePassword);
        etTitle.setText(instanceTitle);
        etUrl.setText(instanceUrl);
        etNotes.setText(instanceNotes);

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> {
            String password = etPassword.getText().toString();
            String title = etTitle.getText().toString();
            String url = etUrl.getText().toString();
            String notes = etNotes.getText().toString();
            try {
                savePassword(password, title, url, notes, id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Intent intent = new Intent(CreatePasswordActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> {
            generatePassword();
        });

        buttonTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Si la contraseña es visible, ocultarla
                    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    buttonTogglePassword.setText("Mostrar Contraseña");
                } else {
                    // Si la contraseña está oculta, mostrarla
                    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    buttonTogglePassword.setText("Ocultar Contraseña");
                }
                // Cambiar el estado de visibilidad
                isPasswordVisible = !isPasswordVisible;

                // Mover el cursor al final del texto
                etPassword.setSelection(etPassword.length());
            }
        });
    }

    private void savePassword(String password, String title, String url, String notes, int id) throws Exception {
        if (password.isEmpty() || title.isEmpty()) {
            Toast.makeText(this, "Parámetros obligatorios vacíos", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("loggedInUserId", 0);

        byte[] encryptedPassword = EncryptionHelper.encrypt(password);

        PasswordInfoDao pwInfoDao = PasswordInfoDao.getInstance();
        if (id == 0) {
            pwInfoDao.insertPasswordInfo(userId, title, url, notes, Base64.getEncoder().encodeToString(encryptedPassword));
        } else {
            pwInfoDao.updatePasswordInfo(id, title, url, notes, Base64.getEncoder().encodeToString(encryptedPassword));
        }


    }

    private void generatePassword() {
        CheckBox cbUpper = findViewById(R.id.checkBoxUppercase);
        CheckBox cbDigits = findViewById(R.id.checkBoxDigits);
        CheckBox cbSpecial = findViewById(R.id.checkBoxSpecialChars);

        EditText etPassword = findViewById(R.id.editTextPassword);

        String password = PasswordGenerator.generate(20, cbUpper.isChecked(), cbDigits.isChecked(), cbSpecial.isChecked());
        etPassword.setText(password);
    }

}

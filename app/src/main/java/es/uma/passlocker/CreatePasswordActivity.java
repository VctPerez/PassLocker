package es.uma.passlocker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CreatePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(v -> {
            savePassword();
            Intent intent = new Intent(CreatePasswordActivity.this, MainActivity.class);
            startActivity(intent);
        });
        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> {
            generatePassword();

        });
    }

    private void savePassword() {
        // Save password
    }

    private void generatePassword() {
        // Generate password
    }

}

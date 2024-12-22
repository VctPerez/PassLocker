package es.uma.passlocker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Locale;

import es.uma.passlocker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private Button authKeysButton, passwordsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authKeysButton = findViewById(R.id.auth_keys_button);
        passwordsButton = findViewById(R.id.passwords_button);

        // Buttons redirections
        authKeysButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AuthKeysActivity.class);
            startActivity(intent);
        });

        passwordsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PasswordsActivity.class);
            startActivity(intent);
        });
    }
}
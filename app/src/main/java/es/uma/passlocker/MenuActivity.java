package es.uma.passlocker;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import es.uma.passlocker.databinding.ActivityMenuBinding;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.main_layout).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Button btnCreatePassword = findViewById(R.id.goToCreateButton);
        btnCreatePassword.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, CreatePasswordActivity.class);
            startActivity(intent);
        });

        Button btnListPasswords = findViewById(R.id.goToListButton);
        btnListPasswords.setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PasswordListActivity.class);
            startActivity(intent);
        });
    }

}
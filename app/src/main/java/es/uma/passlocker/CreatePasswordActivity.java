package es.uma.passlocker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Base64;

import es.uma.passlocker.db.dao.PasswordInfoDao;
import es.uma.passlocker.keyStore.EncryptionHelper;
import es.uma.passlocker.utils.PasswordUtils;

public class CreatePasswordActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private SeekBar sbLength;
    private TextView tvLength;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int id = getIntent().getIntExtra("id", 0);
        String instanceTitle = getIntent().getStringExtra("site_name");
        String instancePassword = getIntent().getStringExtra("password");
        String instanceUrl = getIntent().getStringExtra("site_url");
        String instanceNotes = getIntent().getStringExtra("notes");

        setContentView(R.layout.activity_create_password);
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.main_layout).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        Button buttonTogglePassword = findViewById(R.id.buttonTogglePassword);
        Button buttonDelete = findViewById(R.id.buttonDelete);
        if (id == 0) buttonDelete.setVisibility(View.INVISIBLE);
        EditText etPassword = findViewById(R.id.editTextPassword);
        EditText etTitle = findViewById(R.id.editTextTitle);
        EditText etUrl = findViewById(R.id.editTextURL);
        EditText etNotes = findViewById(R.id.editTextNotes);
        ProgressBar passwordStrengthBar = findViewById(R.id.passwordStrengthBar);

        etPassword.setText(instancePassword);
        if(!etPassword.getText().toString().isEmpty()){
            int strength = PasswordUtils.checkStrength(etPassword.getText().toString());
            passwordStrengthBar.setProgress(strength);
            passwordStrengthBar.setProgressTintList(calculateColor(strength));
        }
        etTitle.setText(instanceTitle);
        etUrl.setText(instanceUrl);
        etNotes.setText(instanceNotes);

        Button saveButton = findViewById(R.id.buttonSave);

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No hacer nada
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No hacer nada
            }

            @Override
            public void afterTextChanged(Editable s) {
                int strength = PasswordUtils.checkStrength(s.toString());
                passwordStrengthBar.setProgress(strength);

                passwordStrengthBar.setProgressTintList(calculateColor(strength));
            }
        });

        saveButton.setOnClickListener(v -> {
            String password = etPassword.getText().toString();
            String title = etTitle.getText().toString();
            String url = etUrl.getText().toString();
            String notes = etNotes.getText().toString();
            try {
                savePassword(password, title, url, notes, id);
                Intent intent = new Intent(CreatePasswordActivity.this, MenuActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                String okButton = getString(R.string.okButton);
                String errorMesage = getString(R.string.emptyPasswordFields);
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(errorMesage)
                        .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            PasswordInfoDao pwInfoDao = PasswordInfoDao.getInstance();
            pwInfoDao.deletePasswordInfo(id);
            Toast.makeText(this, getString(R.string.successDelete), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreatePasswordActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> {
            try{
                generatePassword();

            }catch (Exception e){
                String okButton = getString(R.string.okButton);
                String errorMesage = getString(R.string.generatePasswordException);
                new AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage(errorMesage)
                        .setPositiveButton(okButton, (dialog, which) -> dialog.dismiss())
                        .show();
            }
        });

        buttonTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Si la contraseña es visible, ocultarla
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                buttonTogglePassword.setText(getString(R.string.show_password));
            } else {
                // Si la contraseña está oculta, mostrarla
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                buttonTogglePassword.setText(getString(R.string.hidePassword));
            }
            // Cambiar el estado de visibilidad
            isPasswordVisible = !isPasswordVisible;

            // Mover el cursor al final del texto
            etPassword.setSelection(etPassword.length());
        });

        sbLength = findViewById(R.id.seekBarPasswordLength);
        tvLength = findViewById(R.id.lengthTag);
        tvLength.setText(getString(R.string.lengthTag) + " " + sbLength.getProgress());
        sbLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvLength.setText(getString(R.string.lengthTag) + " " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // No hacer nada
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // No hacer nada
            }
        });

    }

    private void savePassword(String password, String title, String url, String notes, int id) throws Exception {
        if (password.isEmpty() || title.isEmpty()) {
            throw new Exception();
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
        String successMessage = getString(R.string.successSave);
        Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
    }

    private void generatePassword() {
        CheckBox cbUpper = findViewById(R.id.checkBoxUppercase);
        CheckBox cbDigits = findViewById(R.id.checkBoxDigits);
        CheckBox cbSpecial = findViewById(R.id.checkBoxSpecialChars);

        EditText etPassword = findViewById(R.id.editTextPassword);

        String password = PasswordUtils.generate(sbLength.getProgress(), cbUpper.isChecked(), cbDigits.isChecked(), cbSpecial.isChecked());
        etPassword.setText(password);
        String successMessage = getString(R.string.successGeneration);
        Snackbar.make(findViewById(R.id.main_layout), successMessage, Snackbar.LENGTH_LONG).show();
    }

    private ColorStateList calculateColor(int strength){
        ColorStateList color;
        if(strength == 25) {
            color = ColorStateList.valueOf(Color.DKGRAY);
        }else if (strength == 50) {
            color = ColorStateList.valueOf(Color.RED);
        }else if (strength == 75) {
            color = ColorStateList.valueOf(Color.GREEN);
        }else{
            color = ColorStateList.valueOf(Color.MAGENTA);
        }
        return color;
    }

}

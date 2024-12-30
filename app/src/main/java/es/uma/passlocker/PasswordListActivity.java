package es.uma.passlocker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import es.uma.passlocker.db.dao.PasswordInfoDao;
import es.uma.passlocker.db.entities.PasswordInfoEntity;
import es.uma.passlocker.keyStore.EncryptionHelper;
import es.uma.passlocker.ui.adapter.NameAdapter;

public class PasswordListActivity extends AppCompatActivity {

    private RecyclerView rvNames;
    private NameAdapter nameAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_list);

        rvNames = findViewById(R.id.rvNames);
        rvNames.setLayoutManager(new LinearLayoutManager(this));
        AnimationDrawable animationDrawable = (AnimationDrawable) findViewById(R.id.main_layout).getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        List<String> nameList = loadNamesFromDatabase();

        nameAdapter = new NameAdapter(nameList, name -> {
            PasswordInfoDao pwInfoDao = PasswordInfoDao.getInstance();
            PasswordInfoEntity password = pwInfoDao.getPasswordInfo(name);

            Intent intent = new Intent(PasswordListActivity.this, CreatePasswordActivity.class);
            intent.putExtra("id", password.getId());
            intent.putExtra("site_name", password.getSiteName());
            intent.putExtra("site_url", password.getSiteUrl());
            intent.putExtra("password", EncryptionHelper.decrypt(Base64.getDecoder().decode(password.getPassword())));
            intent.putExtra("notes", password.getNotes());
            startActivity(intent);
        });
        rvNames.setAdapter(nameAdapter);
    }

    private List<String> loadNamesFromDatabase() {
        List<String> names;
        PasswordInfoDao pwInfoDao = PasswordInfoDao.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("loggedInUserId", 0);

        names = pwInfoDao.getUserPasswords(Integer.toString(userId));
        return names;
    }
}

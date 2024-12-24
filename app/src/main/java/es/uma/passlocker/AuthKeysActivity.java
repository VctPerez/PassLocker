package es.uma.passlocker;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.uma.passlocker.db.entities.PasswordInfoEntity;
import es.uma.passlocker.db.entities.UserEntity;
import es.uma.passlocker.ui.adapter.AuthKeyAdapter;

public class AuthKeysActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AuthKeyAdapter adapter;
    private List<PasswordInfoEntity> authKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_keys);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initList();

        adapter = new AuthKeyAdapter(authKeyList);
        recyclerView.setAdapter(adapter);
    }

    private void initList(){
        authKeyList = new ArrayList<>();
        authKeyList.add(new PasswordInfoEntity(1, new UserEntity(1,"username"), "Steam", "www.google.es", null));

        authKeyList.add(new PasswordInfoEntity(2, new UserEntity(1,"username"), "Epic Games", "www.google.es", "Fornai"));
    }
}
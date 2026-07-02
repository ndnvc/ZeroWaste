package com.example.zerowasteapk;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zerowasteapk.adapter.UserAdapter;
import com.example.zerowasteapk.models.User;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import io.realm.Realm;

public class AdminUserActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private UserAdapter adapter;
    private Realm realm;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        realm = Realm.getDefaultInstance();
        rvUsers = findViewById(R.id.rvAdminUsers);
        toolbar = findViewById(R.id.toolbarAdmin);

        toolbar.setNavigationOnClickListener(v -> finish());

        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        
        List<User> users = realm.where(User.class).findAll();
        adapter = new UserAdapter(realm.copyFromRealm(users));
        rvUsers.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

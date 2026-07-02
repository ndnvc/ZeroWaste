package com.example.zerowasteapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zerowasteapk.adapter.StatusAdapter;
import com.example.zerowasteapk.models.Transaksi;
import com.google.android.material.appbar.MaterialToolbar;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class StatusActivity extends AppCompatActivity {

    private RecyclerView rvStatus;
    private StatusAdapter adapter;
    private MaterialToolbar toolbar;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        realm = Realm.getDefaultInstance();

        rvStatus = findViewById(R.id.rvStatus);
        toolbar = findViewById(R.id.toolbarStatus);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rvStatus.setLayoutManager(new LinearLayoutManager(this));
        loadStatusData();
    }

    private void loadStatusData() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUserEmail = sharedPref.getString("USER_EMAIL", "");

        // Mengambil data transaksi milik user yang sedang login, diurutkan dari yang terbaru
        RealmResults<Transaksi> results = realm.where(Transaksi.class)
                .equalTo("userEmail", currentUserEmail)
                .findAll()
                .sort("tanggal", Sort.DESCENDING);

        List<Transaksi> listStatus = realm.copyFromRealm(results);

        adapter = new StatusAdapter(listStatus);
        rvStatus.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

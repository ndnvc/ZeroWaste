package com.example.zerowasteapk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.zerowasteapk.adapter.MakananAdapter;
import com.example.zerowasteapk.models.Makanan;
import java.util.ArrayList;
import java.util.List;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeName;
    private Button btnNavUpload, btnNavStatus;
    private ImageView ivProfile;
    private RecyclerView rvMakanan;
    private MakananAdapter adapter;
    private List<Makanan> listMakanan;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();

        tvWelcomeName = findViewById(R.id.tvWelcomeName);
        btnNavUpload = findViewById(R.id.btnNavUpload);
        btnNavStatus = findViewById(R.id.btnNavStatus);
        ivProfile = findViewById(R.id.ivProfile);
        rvMakanan = findViewById(R.id.rvMakanan);

        // Ambil nama dari SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userName = sharedPref.getString("USER_NAME", "User");
        tvWelcomeName.setText("Halo, " + userName + "!");

        // Setup RecyclerView
        rvMakanan.setLayoutManager(new LinearLayoutManager(this));
        
        // Load data dari Realm
        loadData();

        btnNavUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });

        btnNavStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatusActivity.class);
                startActivity(intent);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data setiap kali kembali ke MainActivity
        loadData();
    }

    private void loadData() {
        RealmResults<Makanan> results = realm.where(Makanan.class).findAll();
        
        if (results.isEmpty()) {
            // Jika kosong, tambahkan data dummy ke Realm
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(new Makanan(1, "Nasi Goreng Spesial", "2 Porsi", "Jl. Merdeka No. 10", "Masih hangat, sisa acara kantor.", "2 Jam lagi", "Sistem"));
                    realm.copyToRealmOrUpdate(new Makanan(2, "Roti Bakar", "5 Potong", "Kantin Teknik", "Roti tawar bakar cokelat.", "5 Jam lagi", "Sistem"));
                    realm.copyToRealmOrUpdate(new Makanan(3, "Soto Ayam", "1 Bungkus", "Kost Putri Hijau", "Belum disentuh sama sekali.", "1 Jam lagi", "Sistem"));
                }
            });
            results = realm.where(Makanan.class).findAll();
        }

        // Convert RealmResults ke List agar kompatibel dengan adapter saat ini
        listMakanan = realm.copyFromRealm(results);
        
        adapter = new MakananAdapter(listMakanan, new MakananAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Makanan makanan) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("EXTRA_MAKANAN", makanan);
                startActivity(intent);
            }
        });
        rvMakanan.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

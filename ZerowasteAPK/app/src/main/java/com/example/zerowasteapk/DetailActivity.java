package com.example.zerowasteapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zerowasteapk.models.Makanan;
import com.example.zerowasteapk.models.Transaksi;
import com.google.android.material.appbar.MaterialToolbar;
import io.realm.Realm;
import java.util.Date;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {

    private TextView tvNama, tvSisaWaktu, tvDeskripsi, tvLokasi, tvPorsi;
    private Button btnKlaim;
    private MaterialToolbar toolbar;
    private Realm realm;
    private int makananId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getDefaultInstance();

        tvNama = findViewById(R.id.tvDetailNama);
        tvSisaWaktu = findViewById(R.id.tvDetailSisaWaktu);
        tvDeskripsi = findViewById(R.id.tvDetailDeskripsi);
        tvLokasi = findViewById(R.id.tvDetailLokasi);
        tvPorsi = findViewById(R.id.tvDetailPorsi);
        btnKlaim = findViewById(R.id.btnKlaim);
        toolbar = findViewById(R.id.toolbarDetail);

        toolbar.setNavigationOnClickListener(v -> finish());

        // Ambil data dari Intent
        Makanan makananIntent = (Makanan) getIntent().getSerializableExtra("EXTRA_MAKANAN");

        if (makananIntent != null) {
            makananId = makananIntent.getId();
            updateUI(makananIntent);
        }

        btnKlaim.setOnClickListener(v -> {
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userEmail = sharedPref.getString("USER_EMAIL", "Guest");

            realm.executeTransaction(r -> {
                Makanan m = r.where(Makanan.class).equalTo("id", makananId).findFirst();
                if (m != null) {
                    m.setClaimed(true);

                    // Buat Transaksi Baru
                    String transactionId = UUID.randomUUID().toString();
                    Transaksi transaksi = new Transaksi(
                            transactionId,
                            m.getId(),
                            m.getNama(),
                            userEmail,
                            "Berhasil",
                            new Date()
                    );
                    r.copyToRealmOrUpdate(transaksi);
                }
            });
            
            Toast.makeText(DetailActivity.this, "Berhasil Mengklaim Makanan!", Toast.LENGTH_SHORT).show();
            
            // Refresh UI setelah update database
            Makanan updatedMakanan = realm.where(Makanan.class).equalTo("id", makananId).findFirst();
            if (updatedMakanan != null) {
                updateUI(updatedMakanan);
            }
        });
    }

    private void updateUI(Makanan makanan) {
        tvNama.setText(makanan.getNama());
        tvSisaWaktu.setText("Sisa Waktu: " + makanan.getSisaWaktu());
        tvDeskripsi.setText(makanan.getDeskripsi());
        tvLokasi.setText(makanan.getLokasi());
        tvPorsi.setText(makanan.getPorsi());

        if (makanan.isClaimed()) {
            btnKlaim.setEnabled(false);
            btnKlaim.setText("Sudah Diklaim");
            btnKlaim.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

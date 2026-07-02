package com.example.zerowasteapk;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.zerowasteapk.models.Makanan;
import io.realm.Realm;

public class UploadActivity extends AppCompatActivity {

    private EditText etNama, etPorsi, etLokasi, etDeskripsi;
    private Button btnSubmit, btnBatal;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        realm = Realm.getDefaultInstance();

        etNama = findViewById(R.id.etNamaMakanan);
        etPorsi = findViewById(R.id.etPorsi);
        etLokasi = findViewById(R.id.etLokasi);
        etDeskripsi = findViewById(R.id.etDeskripsi);
        btnSubmit = findViewById(R.id.btnSubmitDonasi);
        btnBatal = findViewById(R.id.btnBatal);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nama = etNama.getText().toString();
                final String porsi = etPorsi.getText().toString();
                final String lokasi = etLokasi.getText().toString();
                final String deskripsi = etDeskripsi.getText().toString();

                if (!nama.isEmpty() && !porsi.isEmpty() && !lokasi.isEmpty() && !deskripsi.isEmpty()) {
                    
                    // Ambil nama pengunggah dari SharedPreferences
                    SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    final String currentUserName = sharedPref.getString("USER_NAME", "Anonim");

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Number currentIdNum = realm.where(Makanan.class).max("id");
                            int nextId = (currentIdNum == null) ? 1 : currentIdNum.intValue() + 1;
                            
                            // Simpan dengan nama pengunggah
                            Makanan makanan = new Makanan(nextId, nama, porsi, lokasi, deskripsi, "Baru saja", currentUserName);
                            realm.copyToRealmOrUpdate(makanan);
                        }
                    });

                    Toast.makeText(UploadActivity.this, "Donasi Berhasil di-Upload!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UploadActivity.this, "Lengkapi form!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
    }
}

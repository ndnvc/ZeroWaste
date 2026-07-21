package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private MaterialCardView btnViewFood, btnUploadFood, cardHistory;
    private MaterialButton btnSaveAddress;
    private ImageView btnViewProfile;
    private Spinner spUserLocation;
    private UserRepository userRepository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        userRepository = new UserRepository(this);

        // Menghubungkan variabel dengan ID di XML baru
        tvWelcome = findViewById(R.id.tvWelcome);
        btnViewFood = findViewById(R.id.btnViewFood);
        btnUploadFood = findViewById(R.id.btnUploadFood);
        cardHistory = findViewById(R.id.cardHistory);
        spUserLocation = findViewById(R.id.spUserLocation);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);
        btnViewProfile = findViewById(R.id.btnViewProfile);

        currentUserId = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");

        // Menampilkan nama user
        if (userName != null && !userName.isEmpty()) {
            tvWelcome.setText("Halo, " + userName + "!");
        } else {
            tvWelcome.setText("Halo, Pengguna!");
        }

        // Agar teks Spinner yang terpilih berwarna putih di atas latar belakang gelap
        spUserLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Aksi simpan lokasi
        btnSaveAddress.setOnClickListener(v -> {
            String address = spUserLocation.getSelectedItem().toString();
            if (currentUserId != null) {
                userRepository.updateUserAddress(currentUserId, address);
                Toast.makeText(this, "Lokasi disimpan", Toast.LENGTH_SHORT).show();
            }
        });

        // Aksi navigasi menu
        btnViewFood.setOnClickListener(v -> {
            Intent i = new Intent(this, FoodListActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        });

        btnUploadFood.setOnClickListener(v -> {
            Intent i = new Intent(this, UploadFoodActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        });

        // Penanganan klik ganda untuk Riwayat (mencegah event klik terhalang oleh child layout)
        View.OnClickListener historyClickListener = v -> {
            Intent i = new Intent(this, HistoryActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        };

        if (cardHistory != null) {
            cardHistory.setOnClickListener(historyClickListener);
        }

        View btnHistory = findViewById(R.id.btnHistory);
        if (btnHistory != null) {
            btnHistory.setOnClickListener(historyClickListener);
        }

        btnViewProfile.setOnClickListener(v -> {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        });
    }
}
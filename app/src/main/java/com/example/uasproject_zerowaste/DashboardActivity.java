package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout btnViewFood, btnUploadFood, btnHistory;
    private MaterialButton btnSaveAddress, btnViewProfile;
    private Spinner spUserLocation;
    private UserRepository userRepository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        userRepository = new UserRepository(this);

        // Menghubungkan variabel dengan ID di XML
        tvWelcome = findViewById(R.id.tvWelcome);
        btnViewFood = findViewById(R.id.btnViewFood);
        btnUploadFood = findViewById(R.id.btnUploadFood);
        btnHistory = findViewById(R.id.btnHistory);
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

        // Aksi tombol
        btnSaveAddress.setOnClickListener(v -> {
            String address = spUserLocation.getSelectedItem().toString();
            if (currentUserId != null) {
                userRepository.updateUserAddress(currentUserId, address);
                Toast.makeText(this, "Lokasi disimpan", Toast.LENGTH_SHORT).show();
            }
        });

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

        btnHistory.setOnClickListener(v -> {
            Intent i = new Intent(this, HistoryActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        });

        btnViewProfile.setOnClickListener(v -> {
            Intent i = new Intent(this, ProfileActivity.class);
            i.putExtra("USER_ID", currentUserId);
            startActivity(i);
        });
    }
}

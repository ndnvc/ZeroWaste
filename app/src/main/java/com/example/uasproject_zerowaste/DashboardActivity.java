package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout btnViewFood, btnUploadFood;
    private MaterialButton btnGoToProfile, btnSaveAddress;
    private Spinner spUserLocation;
    private UserRepository userRepository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        userRepository = new UserRepository(this);
        tvWelcome = findViewById(R.id.tvWelcome);
        btnViewFood = findViewById(R.id.btnViewFood);
        btnUploadFood = findViewById(R.id.btnUploadFood);
        btnGoToProfile = findViewById(R.id.btnGoToProfile);
        spUserLocation = findViewById(R.id.spUserLocation);
        btnSaveAddress = findViewById(R.id.btnSaveAddress);

        currentUserId = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");

        if (currentUserId != null) {
            String savedAddress = userRepository.getUserAddress(currentUserId);
            if (savedAddress != null && !savedAddress.isEmpty()) {
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spUserLocation.getAdapter();
                int pos = adapter.getPosition(savedAddress);
                if (pos >= 0) {
                    spUserLocation.setSelection(pos);
                }
            }
        }

        btnSaveAddress.setOnClickListener(v -> {
            String address = spUserLocation.getSelectedItem().toString();
            if (currentUserId != null) {
                userRepository.updateUserAddress(currentUserId, address);
                Toast.makeText(this, "Lokasi berhasil diperbarui ke " + address, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sesi berakhir, silakan login kembali.", Toast.LENGTH_SHORT).show();
            }
        });

        if (userName != null && !userName.isEmpty()) {
            tvWelcome.setText("Halo, " + userName + "!");
        } else {
            tvWelcome.setText("Halo, Selamat Datang!");
        }

        btnViewFood.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FoodListActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

        btnUploadFood.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UploadFoodActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });

        btnGoToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", currentUserId);
            startActivity(intent);
        });
    }
}
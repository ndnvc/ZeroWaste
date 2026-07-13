package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private LinearLayout btnViewFood, btnUploadFood;
    private MaterialButton btnGoToProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnViewFood = findViewById(R.id.btnViewFood);
        btnUploadFood = findViewById(R.id.btnUploadFood);
        btnGoToProfile = findViewById(R.id.btnGoToProfile);

        String userId = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");

        if (userName != null && !userName.isEmpty()) {
            tvWelcome.setText("Halo, " + userName + "!");
        } else {
            tvWelcome.setText("Halo, Selamat Datang!");
        }

        btnViewFood.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, FoodListActivity.class);
            startActivity(intent);
        });

        btnUploadFood.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, UploadFoodActivity.class);
            startActivity(intent);
        });

        btnGoToProfile.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
        });
    }
}
package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class activity_upload_food extends AppCompatActivity {

    private EditText etFoodName, etQuantity, etExpiry, etDescription;
    private Spinner spUploadLocation;
    private MaterialButton btnSubmitFood;
    private UserRepository userRepository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload_food);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);
        currentUserId = getIntent().getStringExtra("USER_ID");

        etFoodName = findViewById(R.id.etFoodName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        etDescription = findViewById(R.id.etDescription);
        spUploadLocation = findViewById(R.id.spUploadLocation);
        btnSubmitFood = findViewById(R.id.btnSubmitFood);

        btnSubmitFood.setOnClickListener(v -> {
            String name = etFoodName.getText().toString().trim();
            String qtyStr = etQuantity.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();
            String location = spUploadLocation.getSelectedItem().toString();
            String desc = etDescription.getText().toString().trim();

            if (name.isEmpty() || qtyStr.isEmpty() || expiry.isEmpty()) {
                Toast.makeText(this, "Mohon isi bidang yang wajib!", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty = Integer.parseInt(qtyStr);

            boolean isSuccess = userRepository.uploadFoodDonation(name, desc, qty, expiry, location, 0.0, currentUserId != null ? currentUserId : "unknown_user");
            if (isSuccess) {
                Toast.makeText(this, "Donasi makanan berhasil di-upload!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal mengunggah data makanan.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userRepository != null) {
            userRepository.close();
        }
    }
}
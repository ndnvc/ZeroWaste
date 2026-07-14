package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UploadFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etQuantity, etExpiry, etDescription;
    private Button btnSubmitFood;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food);

        userRepository = new UserRepository(UploadFoodActivity.this);

        etFoodName = findViewById(R.id.etFoodName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        etDescription = findViewById(R.id.etDescription);
        btnSubmitFood = findViewById(R.id.btnSubmitFood);

        ImageButton btnBack = findViewById(R.id.btnBackUpload);
        btnBack.setOnClickListener(v -> finish());

        btnSubmitFood.setOnClickListener(v -> {
            String name = etFoodName.getText().toString().trim();
            String qtyStr = etQuantity.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();

            if (name.isEmpty() || qtyStr.isEmpty() || expiry.isEmpty()) {
                Toast.makeText(this, "Mohon isi bidang yang wajib!", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty = Integer.parseInt(qtyStr);

            boolean isSuccess = userRepository.uploadFoodDonation(name, desc, qty, expiry);
            if (isSuccess) {
                Toast.makeText(this, "Donasi makanan berhasil di-upload!", Toast.LENGTH_SHORT).show();
                finish(); // Kembali ke Dashboard otomatis
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
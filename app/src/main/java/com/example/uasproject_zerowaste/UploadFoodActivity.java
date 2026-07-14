package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UploadFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etQuantity, etExpiry, etDescription;
    private Spinner spUploadLocation;
    private Button btnSubmitFood;
    private UserRepository userRepository;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_food);

        userRepository = new UserRepository(UploadFoodActivity.this);
        currentUserId = getIntent().getStringExtra("USER_ID");

        etFoodName = findViewById(R.id.etFoodName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        etDescription = findViewById(R.id.etDescription);
        spUploadLocation = findViewById(R.id.spUploadLocation);
        btnSubmitFood = findViewById(R.id.btnSubmitFood);

        // Auto-select user's saved location if available
        if (currentUserId != null) {
            String userAddress = userRepository.getUserAddress(currentUserId);
            if (userAddress != null && !userAddress.isEmpty()) {
                ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spUploadLocation.getAdapter();
                int pos = adapter.getPosition(userAddress);
                if (pos >= 0) {
                    spUploadLocation.setSelection(pos);
                }
            }
        }

        ImageButton btnBack = findViewById(R.id.btnBackUpload);
        btnBack.setOnClickListener(v -> finish());

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
            double distance = 0.0;

            boolean isSuccess = userRepository.uploadFoodDonation(name, desc, qty, expiry, location, distance);
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
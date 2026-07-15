package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class tampilan_barcode extends AppCompatActivity {

    private String donationId, foodName, userId;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tampilan_barcode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);

        // Get data from intent
        donationId = getIntent().getStringExtra("DONATION_ID");
        foodName = getIntent().getStringExtra("FOOD_NAME");
        userId = getIntent().getStringExtra("USER_ID");
        String donorName = getIntent().getStringExtra("DONOR_NAME");
        String location = getIntent().getStringExtra("LOCATION");
        double distance = getIntent().getDoubleExtra("DISTANCE", 0.0);
        String expiry = getIntent().getStringExtra("EXPIRY");
        int qty = getIntent().getIntExtra("QTY", 0);
        String description = getIntent().getStringExtra("DESCRIPTION");

        TextView tvFoodNameLabel = findViewById(R.id.tvFoodNameLabel);
        TextView tvDetailDonor = findViewById(R.id.tvDetailDonor);
        TextView tvDetailLocation = findViewById(R.id.tvDetailLocation);
        TextView tvDetailDistance = findViewById(R.id.tvDetailDistance);
        TextView tvDetailExpiry = findViewById(R.id.tvDetailExpiry);
        TextView tvDetailQty = findViewById(R.id.tvDetailQty);
        TextView tvDetailDescription = findViewById(R.id.tvDetailDescription);

        tvFoodNameLabel.setText(foodName);
        tvDetailDonor.setText("Donatur: " + (donorName != null ? donorName : "Anonim"));
        tvDetailLocation.setText("Lokasi: " + (location != null ? location : "-"));
        tvDetailDistance.setText("Jarak: " + distance + " km");
        tvDetailExpiry.setText("Batas Waktu: " + (expiry != null ? expiry : "-"));
        tvDetailQty.setText("Sisa: " + qty + " Pax");
        tvDetailDescription.setText("Deskripsi: " + (description != null && !description.isEmpty() ? description : "Tidak ada deskripsi."));

        MaterialButton btnSimulateDone = findViewById(R.id.btnSimulateDone);
        MaterialButton btnBatal = findViewById(R.id.btnBatal);

        btnSimulateDone.setOnClickListener(v -> {
            // Simulate claiming the food (reducing quantity)
            boolean success = userRepository.claimFoodDonation(donationId, userId);
            if (success) {
                Toast.makeText(this, "Pengambilan makanan berhasil disimulasikan!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Gagal memproses klaim. Mungkin stok habis.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBatal.setOnClickListener(v -> {
            Toast.makeText(this, "Pengambilan dibatalkan.", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
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

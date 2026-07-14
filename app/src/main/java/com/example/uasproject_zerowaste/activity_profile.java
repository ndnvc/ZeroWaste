package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class activity_profile extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone;
    private MaterialButton btnLogout;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userRepository = new UserRepository(this);
        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        btnLogout = findViewById(R.id.btnLogout);

        String userId = getIntent().getStringExtra("USER_ID");

        if (userId != null) {
            User user = userRepository.getUserById(userId);
            if (user != null) {
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
                tvPhone.setText(user.getPhoneNumber());
            }
        }

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Berhasil keluar!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
package com.example.uasproject_zerowaste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoToRegister;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // UPDATE: Menambahkan Context (LoginActivity.this) agar SQLite bisa diinisialisasi
        userRepository = new UserRepository(LoginActivity.this);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToRegister = findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                User user = userRepository.loginUser(email, password);
                if (user != null) {
                    String userId = user.getUserId();
                    String userName = user.getName();

                    Toast.makeText(this, "Selamat datang, " + userName, Toast.LENGTH_SHORT).show();

                    // Pindah ke Dashboard Utama
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.putExtra("USER_ID", userId);
                    intent.putExtra("USER_NAME", userName);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error Autentikasi: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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
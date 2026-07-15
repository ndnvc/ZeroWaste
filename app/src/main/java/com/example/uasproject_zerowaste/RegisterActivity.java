package com.example.uasproject_zerowaste;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etPhone;
    // UPDATE: Menambahkan variabel btnGoToLogin
    private Button btnRegister, btnGoToLogin;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(RegisterActivity.this);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        // Inisialisasi tombol baru
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        // Aksi tombol daftar akun
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifikasi email harus menggunakan @gmail.com
            if (!email.toLowerCase().endsWith("@gmail.com")) {
                Toast.makeText(this, "Email harus menggunakan domain @gmail.com", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isSuccess = userRepository.registerUser(name, email, password, phone);
            if (isSuccess) {
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Email tersebut sudah terdaftar!", Toast.LENGTH_SHORT).show();
            }
        });

        // UPDATE: Aksi untuk tombol kembali ke Login
        btnGoToLogin.setOnClickListener(v -> {
            finish(); // Menutup halaman register dan langsung kembali ke LoginActivity
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
package com.example.zerowasteapk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString()
            val passwordInput = etPassword.text.toString()

            if (emailInput.isNotEmpty() && passwordInput.isNotEmpty()) {
                val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val savedEmail = sharedPref.getString("USER_EMAIL", null)
                val savedPassword = sharedPref.getString("USER_PASSWORD", null)

                if (emailInput == savedEmail && passwordInput == savedPassword) {
                    Toast.makeText(this, "Selamat Datang!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (savedEmail == null) {
                    Toast.makeText(this, "Akun belum terdaftar. Silakan Register dulu.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Mohon isi Email dan Password", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
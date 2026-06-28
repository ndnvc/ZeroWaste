package com.example.zerowasteapk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.zerowasteapk.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Mengambil nama dari SharedPreferences
            val context = LocalContext.current
            val sharedPref = remember { context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE) }
            val userName = remember { sharedPref.getString("USER_NAME", "User") ?: "User" }

            // Memanggil fungsi utama aplikasi
            ZeroWasteApp(userName) {
                // Logout logic: Kembali ke LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun ZeroWasteApp(userName: String, onLogout: () -> Unit) {
    // Logika Navigasi Sederhana menggunakan State
    var currentScreen by remember { mutableStateOf("dashboard") }
    
    // State untuk menyimpan daftar makanan secara dinamis (Lokal)
    var listMakanan by remember { 
        mutableStateOf(
            listOf(
                Makanan(1, "Nasi Goreng Spesial", "2 Porsi", "Jl. Merdeka No. 10", "Masih hangat, sisa acara kantor.", "2 Jam lagi"),
                Makanan(2, "Roti Bakar", "5 Potong", "Kantin Teknik", "Roti tawar bakar cokelat.", "5 Jam lagi"),
                Makanan(3, "Soto Ayam", "1 Bungkus", "Kost Putri Hijau", "Belum disentuh sama sekali.", "1 Jam lagi")
            )
        )
    }
    
    // State untuk menyimpan makanan yang sedang dilihat detailnya
    var selectedMakanan by remember { mutableStateOf<Makanan?>(null) }

    Scaffold { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "dashboard" -> DashboardScreen(
                    userName = userName,
                    listMakanan = listMakanan.filter { !it.isClaimed },
                    onNavigateToUpload = { currentScreen = "upload" },
                    onNavigateToStatus = { currentScreen = "status" },
                    onNavigateToDetail = { makanan ->
                        selectedMakanan = makanan
                        currentScreen = "detail"
                    },
                    onNavigateToProfile = { currentScreen = "profile" }
                )
                "upload" -> UploadDonasiScreen(
                    onBack = { currentScreen = "dashboard" },
                    onUploadSuccess = { baru ->
                        listMakanan = listMakanan + baru
                        currentScreen = "dashboard"
                    }
                )
                "status" -> StatusScreen(
                    listMakanan = listMakanan,
                    onBack = { currentScreen = "dashboard" }
                )
                "profile" -> ProfileScreen(
                    userName = userName,
                    onBack = { currentScreen = "dashboard" },
                    onLogout = onLogout
                )
                "detail" -> selectedMakanan?.let { makanan ->
                    DetailMakananScreen(
                        makanan = makanan,
                        onBack = { currentScreen = "dashboard" },
                        onClaimSuccess = { claimedId ->
                            listMakanan = listMakanan.map {
                                if (it.id == claimedId) it.copy(isClaimed = true) else it
                            }
                            currentScreen = "dashboard"
                        }
                    )
                }
            }
        }
    }
}

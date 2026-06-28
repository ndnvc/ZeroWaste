package com.example.zerowasteapk.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UploadDonasiScreen(
    onBack: () -> Unit,
    onUploadSuccess: (Makanan) -> Unit
) {
    // State untuk menampung input user
    var namaMakanan by remember { mutableStateOf("") }
    var jumlahPorsi by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Form Upload Donasi", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = namaMakanan,
            onValueChange = { namaMakanan = it },
            label = { Text("Nama Makanan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = jumlahPorsi,
            onValueChange = { jumlahPorsi = it },
            label = { Text("Jumlah / Porsi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = lokasi,
            onValueChange = { lokasi = it },
            label = { Text("Lokasi Pengambilan") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text("Deskripsi Makanan") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (namaMakanan.isNotEmpty() && jumlahPorsi.isNotEmpty() && lokasi.isNotEmpty() && deskripsi.isNotEmpty()) {
                    val baru = Makanan(
                        id = (0..1000).random(), // ID acak sederhana
                        nama = namaMakanan,
                        porsi = jumlahPorsi,
                        lokasi = lokasi,
                        deskripsi = deskripsi,
                        sisaWaktu = "3 Jam lagi"
                    )
                    onUploadSuccess(baru)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Submit Donasi")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Batal")
        }
    }
}

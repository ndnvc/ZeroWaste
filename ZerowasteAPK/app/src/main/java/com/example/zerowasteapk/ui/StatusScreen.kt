package com.example.zerowasteapk.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusScreen(
    listMakanan: List<Makanan>,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = onBack) {
                Text("Kembali")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Status Saya", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Daftar Donasi & Klaim", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listMakanan) { makanan ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (makanan.isClaimed) Color(0xFFE8F5E9) else Color.White
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = makanan.nama, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (makanan.isClaimed) "Status: Sudah Diklaim / Selesai" else "Status: Tersedia",
                            color = if (makanan.isClaimed) Color(0xFF2E7D32) else Color(0xFFFFA000)
                        )
                        Text(text = "Lokasi: ${makanan.lokasi}", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

package com.example.zerowasteapk.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DetailMakananScreen(
    makanan: Makanan,
    onBack: () -> Unit,
    onClaimSuccess: (Int) -> Unit
) {
    // Gunakan status dari objek makanan
    val isAlreadyClaimed = makanan.isClaimed

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) {
            Text("Kembali")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = makanan.nama, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(text = "Sisa Waktu: ${makanan.sisaWaktu}", color = Color.Red)

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Deskripsi:", fontWeight = FontWeight.Bold)
                Text(text = makanan.deskripsi)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(text = "Lokasi:", fontWeight = FontWeight.Bold)
                Text(text = makanan.lokasi)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "Porsi:", fontWeight = FontWeight.Bold)
                Text(text = makanan.porsi)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { onClaimSuccess(makanan.id) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !isAlreadyClaimed,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAlreadyClaimed) Color.Gray else Color(0xFF2E7D32)
            )
        ) {
            Text(text = if (isAlreadyClaimed) "Sudah Diklaim" else "Klaim Makanan")
        }
    }
}

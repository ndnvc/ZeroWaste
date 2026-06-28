package com.example.zerowasteapk.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    userName: String,
    listMakanan: List<Makanan>,
    onNavigateToUpload: () -> Unit,
    onNavigateToStatus: () -> Unit,
    onNavigateToDetail: (Makanan) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Bagian Atas: Info Profile Singkat
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)) // Hijau Tema Sosial
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Halo, $userName!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Poin Kamu: 150 pts", color = Color.White, fontSize = 16.sp)
                }
                IconButton(onClick = onNavigateToProfile) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Lihat Profil",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Tengah: Tombol Cepat (Upload & Status)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onNavigateToUpload,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)) // Oranye untuk Aksi
            ) {
                Text("Upload Donasi")
            }
            Button(
                onClick = onNavigateToStatus,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cek Status")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Bagian Bawah: List Makanan Tersedia menggunakan LazyColumn (seperti RecyclerView)
        Text(text = "Makanan Tersedia Terdekat", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(listMakanan) { makanan ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = makanan.nama, fontWeight = FontWeight.Bold)
                            Text(text = "Porsi: ${makanan.porsi}")
                        }
                        Button(onClick = { onNavigateToDetail(makanan) }) {
                            Text("Detail")
                        }
                    }
                }
            }
        }
    }
}

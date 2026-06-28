package com.example.zerowasteapk.ui

// Model data sederhana untuk aplikasi ZeroWaste
data class Makanan(
    val id: Int = 0,
    val nama: String = "",
    val porsi: String = "",
    val lokasi: String = "",
    val deskripsi: String = "",
    val sisaWaktu: String = "",
    val isClaimed: Boolean = false
)

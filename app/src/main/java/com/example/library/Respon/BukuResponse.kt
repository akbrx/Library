package com.example.library.Respon

data class BukuResponse(
    val status: String,
    val message: String,
    val data: List<Buku>
)

data class Buku(
    val id: Int,
    val nama_buku: String,
    val deskripsi: String,
    val imageUrl: String
)


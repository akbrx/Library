package com.example.library.Respon

data class LoanResponse(
    val meta: Meta,
    val data: List<Loan>
)

data class Meta(
    val message: String,
    val status: Boolean
)

data class Loan(
    val id: Int,
    val member_id: Int,
    val member_nama: String,
    val book_id: Int,
    val book_nama: String,
    val peminjaman: String,
    val pengembalian: String,
    val waktu_request: String
)

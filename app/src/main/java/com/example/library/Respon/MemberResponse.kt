package com.example.library.Respon

data class MemberResponse(
    val data_user: List<Member>?,
    val meta: Meta
)

data class Member(
    val id: Int,
    val email: String,
    val username: String,
    val location: String,
    val phone: String,
    val gender: String
)

package com.example.library.Respon

data class RegisterResponse(
	val data: Data? = null,
	val message: String? = null,
	val status: Boolean? = null
)

data class Data(
	val email: String? = null,
	val username: String? = null
)

data class ApiResponse(
	val response: Boolean,
	val message: String,
	val payload: Payload?
)

data class Payload(
	val fullname: String,
	val email: String
)



package com.example.library.API

data class LoginResponse(
	val status: Boolean,
	val message: String,
	val payload: User?
)

data class User(
	val username: String,
	val password: String,
	val email: String
)


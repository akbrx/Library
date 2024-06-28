package com.example.library

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.library.API.LoginResponse
import com.example.library.API.User
import com.example.library.API.apiclient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailLogin: EditText
    private lateinit var passwordLogin: EditText
    private lateinit var buttonLogin: Button
    private lateinit var text3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailLogin = findViewById(R.id.emailLogin)
        passwordLogin = findViewById(R.id.passwordLogin)
        buttonLogin = findViewById(R.id.buttonLogin)
        text3 = findViewById(R.id.text3)

        buttonLogin.setOnClickListener {
            val checkEmail = emailLogin.text.toString()
            val checkPassword = passwordLogin.text.toString()

            when {
                checkEmail.isEmpty() -> {
                    emailLogin.error = "Please fill out"
                }

                checkPassword.isEmpty() -> {
                    passwordLogin.error = "Please fill out"
                }

                !Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches() -> {
                    emailLogin.error = "Email must contain '@'. Please try again."
                }

                else -> {
                    if (checkPassword.length >= 8) {
                        loginUser(checkEmail, checkPassword)
                    } else {
                        Toast.makeText(
                            this,
                            "Password must be at least 8 characters long",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        text3.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val apiService = apiclient.apiService
        val call = apiService.loginUser(email, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.status == true) {
                        // Simpan data pengguna di SharedPreferences
                        saveUserData(loginResponse.payload)
                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    } else {
                        Toast.makeText(this@LoginActivity, loginResponse?.message ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveUserData(user: User?) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", user?.email)
        editor.putString("username", user?.username)
        // Tambahkan data lain jika diperlukan
        editor.apply()
    }

    private fun navigateToHome() {
        val intent = Intent(this@LoginActivity, bottom_navigation_actyiviy::class.java)
        startActivity(intent)
        finish() // Close LoginActivity
    }
}

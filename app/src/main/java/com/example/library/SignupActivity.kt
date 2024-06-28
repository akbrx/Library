package com.example.library

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.library.API.apiclient
import com.example.library.Respon.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var usernameSignup: EditText
    private lateinit var emailSignup: EditText
    private lateinit var passwordSignup: EditText
    private lateinit var konfirmasiPassword: EditText
    private lateinit var butttonSignup: Button
    private lateinit var text3: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        usernameSignup = findViewById(R.id.usernameSignup)
        emailSignup = findViewById(R.id.emailSignup)
        passwordSignup = findViewById(R.id.passwordSignup)
        konfirmasiPassword = findViewById(R.id.konfirmasiPassword)
        butttonSignup = findViewById(R.id.buttonSignup)
        text3 = findViewById(R.id.text3)

        text3.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        butttonSignup.setOnClickListener {
            val checkUsername = usernameSignup.text.toString()
            val checkEmail = emailSignup.text.toString()
            val checkPassword1 = passwordSignup.text.toString()
            val checkPassword2 = konfirmasiPassword.text.toString()

            when {
                checkUsername.isEmpty() -> {
                    usernameSignup.error = "Please fill out"
                }
                checkEmail.isEmpty() -> {
                    emailSignup.error = "Please fill out"
                }
                checkPassword1.isEmpty() -> {
                    passwordSignup.error = "Please fill out"
                }
                checkPassword2.isEmpty() -> {
                    konfirmasiPassword.error = "Please fill out"
                }
                !Patterns.EMAIL_ADDRESS.matcher(checkEmail).matches() -> {
                    emailSignup.error = "Email must contain '@'. Please try again."
                }
                else -> {
                    if (checkPassword1.length >= 8) {
                        if (checkPassword1 == checkPassword2) {
                            registerUser(checkUsername, checkEmail, checkPassword1)
                        } else {
                            Toast.makeText(this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        val call = apiclient.apiService.register(name, email, password)
        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse?.response == true) {
                        Toast.makeText(this@SignupActivity, "Your account has been created successfully.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SignupActivity, apiResponse?.message ?: "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "Sign-up failed: ${t.message}. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

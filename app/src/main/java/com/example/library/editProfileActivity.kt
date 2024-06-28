package com.example.library

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class editProfileActivity : AppCompatActivity() {

    private lateinit var usernameEP: EditText
    private lateinit var emailEP: EditText
    private lateinit var lokasiEP: EditText
    private lateinit var nohpEP: EditText
    private lateinit var genderEP: EditText
    private lateinit var editPP: ImageView
    private lateinit var panahBack: ImageView
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)


        usernameEP = findViewById(R.id.usernameEP)
        lokasiEP = findViewById(R.id.lokasilEP)
        emailEP = findViewById(R.id.emailEP)
        nohpEP = findViewById(R.id.nohpEP)
        genderEP = findViewById(R.id.genderEP)
        editPP = findViewById(R.id.editPP)
        panahBack = findViewById(R.id.panahBack)
        saveButton = findViewById(R.id.saveButton)

    }
}
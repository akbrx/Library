package com.example.bookloan

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.example.library.R
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class ProcessPinjamActivity : AppCompatActivity() {

    private lateinit var loanDate: EditText
    private lateinit var returnDate: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.process_pinjam)

        // Initialize UI elements
        val bookImage: ImageView = findViewById(R.id.bookImage)
        val bookTitle: TextView = findViewById(R.id.bookTitle)
        val bookDescription: TextView = findViewById(R.id.bookDescription)
        loanDate = findViewById(R.id.loanDate)
        returnDate = findViewById(R.id.returnDate)
        val submitButton: Button = findViewById(R.id.submitButton)

        // Get book information from intent
        val intent = intent
        val title = intent.getStringExtra("bookTitle")
        val description = intent.getStringExtra("bookDescription")
        val imageUrl = intent.getStringExtra("bookImageUrl")
        val memberName = intent.getStringExtra("memberName")

        // Set book information
        bookTitle.text = title
        bookDescription.text = description
        Glide.with(this)
            .load(imageUrl)
            .into(bookImage)



        // Set up date picker for loan date
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLoanDate()
        }

        loanDate.setOnClickListener {
            DatePickerDialog(
                this@ProcessPinjamActivity, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Submit button action
        submitButton.setOnClickListener {
            // Handle the submit action (e.g., save the loan information to a database)
            Toast.makeText(this, "Loan submitted successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateLoanDate() {
        val dateFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        loanDate.setText(sdf.format(calendar.time))

        // Calculate return date (3 days after loan date)
        val returnCalendar = calendar.clone() as Calendar
        returnCalendar.add(Calendar.DAY_OF_MONTH, 3)
        returnDate.text = sdf.format(returnCalendar.time)
    }
}
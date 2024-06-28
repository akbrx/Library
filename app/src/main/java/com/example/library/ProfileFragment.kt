package com.example.library

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.library.Respon.MemberResponse
import com.example.library.API.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment() {

    private lateinit var cardView: CardView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var logOut: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cardView = view.findViewById(R.id.cardView)
        emailTextView = view.findViewById(R.id.emailProfil)
        usernameTextView = view.findViewById(R.id.usernameProfil)
        locationTextView = view.findViewById(R.id.lokasi)
        phoneTextView = view.findViewById(R.id.noHP)
        genderTextView = view.findViewById(R.id.gender)
        logOut = view.findViewById(R.id.logoutLayout)

        // Fetch data from SharedPreferences
        fetchMemberData()

        cardView.setOnClickListener {
            val intent = Intent(requireContext(), editProfileActivity::class.java)
            startActivity(intent)
        }

        logOut.setOnClickListener {
            tampilkanDialogLogout("Are you sure you want to Log Out?")
        }
    }

    private fun fetchMemberData() {
        val sharedPreferences = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val username = sharedPreferences.getString("username", null)

        emailTextView.text = email
        usernameTextView.text = username

        // Fetch other data from API if necessary
        fetchAdditionalData()
    }

    private fun fetchAdditionalData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.10/rest-api/api-php/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getMemberData().enqueue(object : Callback<MemberResponse> {
            override fun onResponse(call: Call<MemberResponse>, response: Response<MemberResponse>) {
                if (response.isSuccessful) {
                    response.body()?.data_user?.firstOrNull()?.let {
                        locationTextView.text = it.location
                        phoneTextView.text = it.phone
                        genderTextView.text = it.gender
                    }
                }
            }

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }


    private fun tampilkanDialogLogout(pesanDialogKP: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.pop_up_log_out)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val teks: TextView = dialog.findViewById(R.id.teksLogout)
        val Cancel: Button = dialog.findViewById(R.id.cancel)
        val Confirm: Button = dialog.findViewById(R.id.confirm)

        pesanDialogKP?.let { teks.text = it }

        Cancel.setOnClickListener { dialog.dismiss() }

        Confirm.setOnClickListener {
            val intent = Intent(dialog.context, MainActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }
}

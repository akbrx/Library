package com.example.library

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookloan.ProcessPinjamActivity
import com.example.library.API.apiclient
import com.example.library.Respon.Buku
import com.example.library.Respon.BukuResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), BookAdapter.OnItemClickListener {

    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookRecyclerView: RecyclerView
    private var fragmentWishlist: FragmentWishlist? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        bookRecyclerView = view.findViewById(R.id.rv_books)

        bookAdapter = BookAdapter(emptyList(), this)
        bookRecyclerView.layoutManager = LinearLayoutManager(context)
        bookRecyclerView.adapter = bookAdapter

        loadBooks()

        return view
    }


    private fun loadBooks() {
        val apiService = apiclient.apiService
        val call = apiService.getBuku()

        call.enqueue(object : Callback<BukuResponse> {
            override fun onResponse(call: Call<BukuResponse>, response: Response<BukuResponse>) {
                val responseBody = response.body()?.toString()
                Log.d("HomeFragment", "API Response: $responseBody")

                if (response.isSuccessful) {
                    val bukuResponse = response.body()
                    if (bukuResponse != null && bukuResponse.status == "success") {
                        val books = bukuResponse.data
                        Log.d("HomeFragment", "Received ${books.size} books")
                        books.forEach { book ->
                            Log.d("HomeFragment", "Book: ${book.nama_buku}, ${book.deskripsi}, ${book.imageUrl}")
                        }
                        bookAdapter.updateBooks(books)
                    } else {
                        Toast.makeText(context, "Failed to load books: ${bukuResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to load books: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BukuResponse>, t: Throwable) {
                Log.e("HomeFragment", "Error loading books", t)
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(book: Buku) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.pop_up, null)

        val NamaBuku = view.findViewById<TextView>(R.id.judul_buku)
        val Deskripsi = view.findViewById<TextView>(R.id.deskripsi_buku)
        val Image = view.findViewById<ImageView>(R.id.cover)
        val bttnPinjam = view.findViewById<Button>(R.id.buttonPinjam)
        val bttnList = view.findViewById<Button>(R.id.buttonList)

        NamaBuku.text = book.nama_buku
        Deskripsi.text = book.deskripsi

        Glide.with(this)
            .load(book.imageUrl)
            .into(Image)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        val sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val memberName = sharedPreferences.getString("memberName", "Default Name")

        bttnPinjam.setOnClickListener {
            val intent = Intent(requireContext(), ProcessPinjamActivity::class.java)
            intent.putExtra("bookTitle", book.nama_buku)
            intent.putExtra("bookDescription", book.deskripsi)
            intent.putExtra("bookImageUrl", book.imageUrl)
            intent.putExtra("memberName", memberName)
            startActivity(intent)
        }

        bttnList.setOnClickListener {
            fragmentWishlist?.addBookToWishlist(book)
            dialog.dismiss()
            Toast.makeText(requireContext(), "${book.nama_buku} added to wishlist", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }
}

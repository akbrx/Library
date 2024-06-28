package com.example.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.Respon.Buku

class FragmentWishlist : Fragment() {

    private lateinit var wishlistAdapter: BookAdapter
    private lateinit var wishlistRecyclerView: RecyclerView
    private var wishlist: MutableList<Buku> = mutableListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        wishlistRecyclerView = view.findViewById(R.id.rv_wishlist)
        wishlistAdapter = BookAdapter(wishlist, null)
        wishlistRecyclerView.layoutManager = LinearLayoutManager(context)
        wishlistRecyclerView.adapter = wishlistAdapter

        return view
    }

    fun addBookToWishlist(book: Buku) {
        wishlist.add(book)
        wishlistAdapter.notifyDataSetChanged()
        Toast.makeText(context, "${book.nama_buku} added to wishlist", Toast.LENGTH_SHORT).show()
    }
}

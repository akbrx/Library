package com.example.library

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.library.Respon.Buku

class BookAdapter(
    private var books: List<Buku>,
    private val itemClickListener: OnItemClickListener?
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(book: Buku)
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.judul_buku)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.deskripsi_buku)
        private val coverImageView: ImageView = itemView.findViewById(R.id.cover)

        fun bind(book: Buku) {
            titleTextView.text = book.nama_buku
            descriptionTextView.text = book.deskripsi
            Glide.with(itemView.context)
                .load(book.imageUrl)
                .into(coverImageView)

            itemView.setOnClickListener {
                itemClickListener?.onItemClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Buku>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
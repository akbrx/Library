import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.library.R
import com.example.library.Respon.Loan
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private var loans: List<Loan>, private val listener: OnItemClickListener) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(loan: Loan)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val namaMemberTextView: TextView = itemView.findViewById(R.id.nama_member)
        private val namaBukuTextView: TextView = itemView.findViewById(R.id.nama_buku)
        private val tanggalPinjamTextView: TextView = itemView.findViewById(R.id.tanggal_pinjam)
        private val tanggalKembaliTextView: TextView = itemView.findViewById(R.id.tanggal_kembali)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(loan: Loan) {
            namaMemberTextView.text = loan.member_nama
            namaBukuTextView.text = loan.book_nama

            // Format tanggal pinjam dan kembali
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            try {
                val tanggalPinjam = dateFormat.format(dateFormat.parse(loan.peminjaman))
                val tanggalKembali = dateFormat.format(dateFormat.parse(loan.pengembalian))

                tanggalPinjamTextView.text = "Pinjam: $tanggalPinjam"
                tanggalKembaliTextView.text = "Kembali: $tanggalKembali"
            } catch (e: Exception) {
                Log.e("HistoryAdapter", "Error parsing dates", e)
            }
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(loans[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loan = loans[position]
        holder.bind(loan)
    }

    override fun getItemCount(): Int {
        return loans.size
    }

    fun updateLoans(newLoans: List<Loan>) {
        loans = newLoans
        notifyDataSetChanged()
    }
}

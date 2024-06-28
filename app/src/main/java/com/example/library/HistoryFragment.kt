import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.library.API.apiclient
import com.example.library.R
import com.example.library.Respon.Loan
import com.example.library.Respon.LoanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment(), HistoryAdapter.OnItemClickListener {

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var loanRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        loanRecyclerView = view.findViewById(R.id.rv_loans)

        historyAdapter = HistoryAdapter(emptyList(), this)
        loanRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        loanRecyclerView.adapter = historyAdapter

        loadLoans()

        return view
    }

    private fun loadLoans() {
        val apiService = apiclient.apiService
        val call = apiService.getLoans()

        call.enqueue(object : Callback<LoanResponse> {
            override fun onResponse(call: Call<LoanResponse>, response: Response<LoanResponse>) {
                if (response.isSuccessful) {
                    val loanResponse = response.body()
                    Log.d("HistoryFragment", "Response body: $loanResponse")
                    if (loanResponse != null) {
                        Log.d("HistoryFragment", "LoanResponse status: ${loanResponse.meta.status}")
                        Log.d("HistoryFragment", "LoanResponse message: ${loanResponse.meta.message}")
                        if (loanResponse.meta.status) {
                            val loans = loanResponse.data
                            Log.d("HistoryFragment", "Received ${loans.size} loans")
                            historyAdapter.updateLoans(loans) // Update the adapter with new data
                        } else {
                            Log.e("HistoryFragment", "Failed to load loans: ${loanResponse.meta.message}")
                            Toast.makeText(requireContext(), "Failed to load loans: ${loanResponse.meta.message}", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("HistoryFragment", "LoanResponse is null")
                        Toast.makeText(requireContext(), "Failed to load loans: response body is null", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("HistoryFragment", "Failed to load loans: ${response.message()}")
                    Toast.makeText(requireContext(), "Failed to load loans: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoanResponse>, t: Throwable) {
                Log.e("HistoryFragment", "Error loading loans", t)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(loan: Loan) {
        Toast.makeText(requireContext(), "Clicked: ${loan.book_nama}", Toast.LENGTH_SHORT).show()
    }
}

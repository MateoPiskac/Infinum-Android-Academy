package infinuma.android.shows.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import infinuma.android.shows.R
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.ActivityShowsBinding

class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShowsBinding.inflate(layoutInflater)

        val showsList = listOf<Show>(
            Show("The Office", getString(R.string.lorem_ipsum) , R.drawable.ic_office),
            Show("Stranger Things", getString(R.string.lorem_ipsum), R.drawable.ic_stranger_things),
            Show("Krv nije voda", getString(R.string.lorem_ipsum), R.drawable.krv_nije_voda_1)
        )
        adapter = ShowsListAdapter(showsList) {


        }
        binding.recyclerView.adapter = adapter


        setContentView(binding.root)
    }
}
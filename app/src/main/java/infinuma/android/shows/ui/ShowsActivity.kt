package infinuma.android.shows.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import infinuma.android.shows.data.SHOW
import infinuma.android.shows.data.showsList
import infinuma.android.shows.databinding.ActivityShowsBinding



class ShowsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowsBinding
    private lateinit var adapter: ShowsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowsBinding.inflate(layoutInflater)
        adapter = ShowsListAdapter(showsList) {
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra(SHOW, it)
            startActivity(intent)
        }
        binding.recyclerView.adapter = adapter
        binding.loadShowsButton.setOnClickListener {
            binding.emptyListIcon.visibility = View.GONE
            binding.emptyListText.visibility = View.GONE
            binding.loadShowsButton.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
        setContentView(binding.root)
    }

    public fun getShowDescription(showName: String): String {
        return showsList.find { it.Title == showName }?.Description ?: "No description available."
    }
}
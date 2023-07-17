package infinuma.android.shows.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import infinuma.android.shows.R
import infinuma.android.shows.data.SHOW
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.ActivityShowDetailsBinding
import java.io.Serializable

class ShowDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityShowDetailsBinding
    lateinit var show: Show

    private var reviewListItemList: MutableList<ReviewListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowDetailsBinding.inflate(layoutInflater)
        val adapter = ReviewListAdapter()
        binding.reviewRecycler.adapter = adapter
        setSupportActionBar(binding.toolbar)
        show = getSerializable(intent, SHOW, Show::class.java)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { this.finish() }
        binding.toolbarLayout.title = show.Title

        reviewListItemList.add(ReviewListItem.ShowDetails(show.Image, show.Description))
        adapter.submitList(reviewListItemList.toList())

        binding.reviewButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
                .setMessage("Are you sure you want to add a new review?")
                .setTitle("Shows app")
                .setCancelable(false)
                .setPositiveButton("Add") { dialog, which ->
                    reviewListItemList.add(
                        ReviewListItem.Review(
                            getString(R.string.placeholder_review_author),
                            getString(R.string.placeholder_review),
                            5
                        )
                    )
                    adapter.submitList(reviewListItemList.toList())
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.cancel()
                }
                .create().show()
        }
        setContentView(binding.root)
    }

    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
    }


}
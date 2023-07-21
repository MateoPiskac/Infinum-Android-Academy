package infinuma.android.shows.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import infinuma.android.shows.R
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.FragmentShowDetailsBinding
import java.io.Serializable

class ShowDetailsFragment : Fragment(R.layout.fragment_show_details) {

    private lateinit var binding: FragmentShowDetailsBinding
    private lateinit var show: Show
    lateinit var adapter: ReviewListAdapter
    private var reviewList: MutableList<ReviewListItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        adapter = ReviewListAdapter()
        binding.reviewRecycler.adapter = adapter
        show = (arguments?.get("show") as? Show)!!
        initToolbar()
        reviewList.add(ReviewListItem.ShowDetails(show.image, show.description))
        reviewList.add(ReviewListItem.NoReviews)
        adapter.submitList(reviewList.toList())

        binding.reviewButton.setOnClickListener {
            val writeReview = WriteReviewDialogFragment()
            reviewList.removeIf { it == ReviewListItem.NoReviews }
            writeReview.getReviews(reviewList)
            writeReview.show(childFragmentManager, "WriteReview")

            adapter.submitList(reviewList.toList())
        }
        return binding.root
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { (activity as MainActivity).back() }
        binding.toolbarLayout.title = show.title
    }

    private val someBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            reviewList = intent.getBundleExtra("reviewList")?.get("reviews") as MutableList<ReviewListItem>
            adapter.submitList(reviewList.toList())
        }
    }
    private fun addReview(author: String, review: String, rating: Int) {
        reviewList.add(
            ReviewListItem.Review(
                author,
                review,
                rating
            )
        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            someBroadcastReceiver,
            IntentFilter("newShowList")
        )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(someBroadcastReceiver)
        super.onPause()
    }

}
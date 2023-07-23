package infinuma.android.shows.ui.showDetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.FragmentShowDetailsBinding

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var show: Show
    lateinit var adapter: ReviewListAdapter
    private var reviewList: MutableList<ReviewListItem> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater)
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
        binding.toolbar.setNavigationOnClickListener { findNavController().navigate(R.id.action_showDetailsFragment_to_showsFragment) }
        binding.toolbarLayout.title = show.title
    }

    private val someBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            reviewList = intent.getBundleExtra("reviewList")?.get("reviews") as MutableList<ReviewListItem>
            adapter.submitList(reviewList.toList())
        }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package infinuma.android.shows.ui.showDetails

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.ShowsApplication
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.FragmentShowDetailsBinding
import infinuma.android.shows.ui.MainActivity
import infinuma.android.shows.ui.showsList.ShowDetailsViewModelFactory

class ShowDetailsFragment : Fragment() {

    private var _binding: FragmentShowDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ReviewListAdapter
    private val viewModel: ShowDetailsViewModel by activityViewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsApplication).database)
    }
    private lateinit var writeReview: WriteReviewDialogFragment
    private lateinit var loading: AlertDialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentShowDetailsBinding.inflate(layoutInflater)
        loading = (activity as MainActivity).initLoadingBarDialog()
        adapter = ReviewListAdapter()
        binding.reviewRecycler.adapter = adapter
        viewModel.setShow((arguments?.get("show") as? Show)!!)
        initToolbar()
        viewModel.showLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }
        loadReviews()
        viewModel.reviewListUpdated.observe(viewLifecycleOwner) {
            adapter.submitList(viewModel.showLiveData.value!!.toList())
        }
        binding.reviewButton.setOnClickListener {
            viewModel.onReviewButtonClick()
            writeReview = WriteReviewDialogFragment()
            writeReview.getShowDetails(viewModel.showLiveData.value?.get(1).toString().toFloat(), viewModel.getShowId())
            writeReview.show(childFragmentManager, "WriteReview")
            writeReview.reviewAdded.observe(viewLifecycleOwner) {
                if (writeReview.reviewAdded.value == true) {
                    writeReview.dismiss()
                    loadReviews()
                    writeReview.reviewAdded.value = false
                }
            }
        }
        viewModel.isLoading.observe(requireActivity()) {
            if (viewModel.isLoading.value == true) {
                loading.window?.setBackgroundDrawableResource(android.R.color.transparent)
                loading.show()
            } else if (viewModel.isLoading.value == false)
                loading.cancel()
        }
        return binding.root
    }

    private fun loadReviews() {
        if ((activity as MainActivity).isInternetConnected()) {
            viewModel.loadReviews()
        } else
            viewModel.loadReviewsFromDatabase(viewModel.getShowId())
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.arrow_back)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigate(R.id.action_showDetailsFragment_to_showsFragment) }
        binding.toolbarLayout.title = viewModel.getShow().title
    }

    private val someBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            viewModel.showLiveData.value?.add(intent.getBundleExtra("reviewList")?.get("reviews") as ReviewListItem.Review)
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
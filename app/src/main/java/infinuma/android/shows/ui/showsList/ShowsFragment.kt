package infinuma.android.shows.ui.showsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.ShowsApplication
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.data.SHOW
import infinuma.android.shows.data.USER_PROFILE
import infinuma.android.shows.databinding.FragmentShowsBinding
import infinuma.android.shows.ui.MainActivity
import infinuma.android.shows.ui.sharedPreferences

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val viewModel: ShowsViewModel by viewModels {
        ShowsViewModelFactory((activity?.application as ShowsApplication).database)
    }
    private val binding get() = _binding!!
    private lateinit var adapter: ShowsListAdapter
    private lateinit var loading: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentShowsBinding.inflate(layoutInflater)
        loading = (activity as MainActivity).initLoadingBarDialog()
        binding.profileButton.isVisible = true
        initAdapter()
        viewModel.showsLiveData.observe(viewLifecycleOwner, Observer { shows ->
            adapter.submitList(shows)
        })
        if ((activity as MainActivity).isInternetConnected()) {
            viewModel.fetchShows()
        } else {
            viewModel.fetchShowsFromDatabase()
        }
        binding.recyclerView.adapter = adapter

        binding.loadShowsButton.setOnClickListener {
            displayShowsList()
        }
        viewModel.isLoading.observe(requireActivity()) {
            if (viewModel.isLoading.value == true) {
                loading.window?.setBackgroundDrawableResource(android.R.color.transparent)
                loading.show()
            } else if (viewModel.isLoading.value == false)
                loading.cancel()
        }
        binding.profileButton.setOnClickListener {
            val userProfile = UserProfileDialogFragment()
            userProfile.show(childFragmentManager, USER_PROFILE)
        }
        return binding.root
    }

    private fun initAdapter() {
        adapter = ShowsListAdapter(emptyList()) {
            val bundle = bundleOf(SHOW to it)
            findNavController().navigate(R.id.action_showsFragment_to_showDetailsFragment, bundle)
        }
    }

    private fun displayShowsList() {
        binding.emptyListIcon.isVisible = false
        binding.emptyListText.isVisible = false
        binding.loadShowsButton.isVisible = false
        binding.recyclerView.isVisible = true

    }

    override fun onResume() {
        super.onResume()
        val tempPath = sharedPreferences.getString(PROFILE_PHOTO_URI, "")
        Glide.with(requireContext()).load(tempPath).placeholder(R.drawable.placeholder_profile_picture)
            .into(binding.profileButton)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

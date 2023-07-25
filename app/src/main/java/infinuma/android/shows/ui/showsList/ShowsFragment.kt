package infinuma.android.shows.ui.showsList

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.data.PROFILEPHOTOURI
import infinuma.android.shows.data.REMEMBER_LOGIN
import infinuma.android.shows.data.SHOW
import infinuma.android.shows.data.USERPROFILE
import infinuma.android.shows.data.showsList
import infinuma.android.shows.databinding.FragmentShowsBinding
import infinuma.android.shows.ui.login.sharedPreferences

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val viewModel = ShowsViewModel()
    private val binding get() = _binding!!
    private lateinit var adapter: ShowsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(layoutInflater)
        binding.profileButton.isVisible = true
        viewModel.fetchShows()
        viewModel.showsLiveData.observe(viewLifecycleOwner, Observer { shows ->
            adapter = ShowsListAdapter(shows) {
                val bundle = bundleOf(SHOW to it)
                findNavController().navigate(R.id.action_showsFragment_to_showDetailsFragment, bundle)
            }
        })
        adapter = ShowsListAdapter(viewModel.showsLiveData.value!!) {
            val bundle = bundleOf(SHOW to it)
            findNavController().navigate(R.id.action_showsFragment_to_showDetailsFragment, bundle)
        }
        binding.recyclerView.adapter = adapter
        binding.loadShowsButton.setOnClickListener {
            loadShows()
        }
        binding.profileButton.setOnClickListener {
            val userProfile = UserProfileDialogFragment()

            userProfile.show(childFragmentManager, USERPROFILE)

        }
        return binding.root
    }

    private fun loadShows() {
        binding.emptyListIcon.isVisible = false
        binding.emptyListText.isVisible = false
        binding.loadShowsButton.isVisible = false
        binding.recyclerView.isVisible = true
    }

    override fun onResume() {
        super.onResume()
        val tempPath = sharedPreferences.getString(PROFILEPHOTOURI,"")
        if(tempPath=="")
            binding.profileButton.setImageResource(R.drawable.placeholder_profile_picture)
        else
            Glide.with(requireContext()).load(Uri.parse(tempPath))
                .into(binding.profileButton)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
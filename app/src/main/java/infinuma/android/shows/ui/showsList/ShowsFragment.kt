package infinuma.android.shows.ui.showsList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import infinuma.android.shows.R
import infinuma.android.shows.data.SHOW
import infinuma.android.shows.data.showsList
import infinuma.android.shows.databinding.FragmentShowsBinding

class ShowsFragment : Fragment() {
    private var _binding: FragmentShowsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ShowsListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowsBinding.inflate(layoutInflater)
        binding.logoutButton.isVisible = true
        binding.logoutButton.setOnClickListener{
            findNavController().navigate(R.id.action_showsFragment_to_loginFragment)
        }
        adapter = ShowsListAdapter(showsList) {
            val bundle = bundleOf(SHOW to it)
            findNavController().navigate(R.id.action_showsFragment_to_showDetailsFragment,bundle)
        }
        binding.recyclerView.adapter = adapter
        binding.loadShowsButton.setOnClickListener {
            binding.emptyListIcon.isVisible = false
            binding.emptyListText.isVisible = false
            binding.loadShowsButton.isVisible = false
            binding.recyclerView.isVisible = true
        }
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
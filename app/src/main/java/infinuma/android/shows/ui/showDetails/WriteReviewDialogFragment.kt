package infinuma.android.shows.ui.showDetails

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.ShowsApplication
import infinuma.android.shows.databinding.FragmentWriteReviewDialogBinding
import infinuma.android.shows.ui.MainActivity
import infinuma.android.shows.ui.showsList.ShowDetailsViewModelFactory
import kotlinx.coroutines.launch

class WriteReviewDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentWriteReviewDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ShowDetailsViewModel by viewModels {
        ShowDetailsViewModelFactory((activity?.application as ShowsApplication).database)
    }
    var reviewAdded : MutableLiveData<Boolean> = MutableLiveData(false)
    private var averageRating: Float = 0f
    private var showId: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWriteReviewDialogBinding.inflate(layoutInflater)
        binding.reviewInputField.setHintTextColor(Color.BLACK)
        binding.closeSheet.setOnClickListener {
            this.dismiss()
        }
        binding.reviewRatingInput.setOnRatingBarChangeListener { _, _, _ ->
            binding.submitReview.isEnabled = true
        }
        binding.submitReview.setOnClickListener {
        if((activity as MainActivity).isInternetConnected()) {
            lifecycleScope.launch {
                try {
                    var response = viewModel.addReview(
                        binding.reviewInputField.text.toString(),
                        binding.reviewRatingInput.rating.toInt(),
                        showId
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                reviewAdded.value = true
            }
        }
            else
                viewModel.addReviewToDatabase(binding.reviewInputField.text.toString(),binding.reviewRatingInput.rating.toInt(),showId)

        }
        return binding.root
    }


    fun getShowDetails(avgRating: Float, showid: Int) {
        averageRating = avgRating
        showId = showid
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package infinuma.android.shows.ui.showDetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.databinding.FragmentWriteReviewDialogBinding
import infinuma.android.shows.models.AddReviewRequest
import infinuma.android.shows.models.User
import infinuma.android.shows.networking.ApiModule
import infinuma.android.shows.ui.sharedPreferences
import kotlinx.coroutines.launch

class WriteReviewDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentWriteReviewDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var review: ReviewListItem.Review
    private var reviews: MutableList<ReviewListItem> = mutableListOf()
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

            lifecycleScope.launch {
                try {
                    var response = addReview(
                        binding.reviewInputField.text.toString(),
                        binding.reviewRatingInput.rating.toInt(),
                        showId
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                reviewAdded.value = true
            }

            review = ReviewListItem.Review(
                sharedPreferences.getString("email", "John Doe")!!,
                binding.reviewInputField.text.toString(),
                binding.reviewRatingInput.rating.toInt(),
                showId,
                User(
                    sharedPreferences.getString("id", "0")!!,
                    sharedPreferences.getString("email", "")!!,
                    sharedPreferences.getString(PROFILE_PHOTO_URI, "")!!
                )
            )

//            val intent = Intent("newShowList")
//            intent.putExtra("reviewList", bundleOf("review" to review))
//            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        return binding.root
    }


    fun getShowDetails(avgRating: Float, showid: Int) {
        averageRating = avgRating
        showId = showid
    }

    private suspend fun addReview(reviewBody: String, rating: Int, showId: Int) =
        ApiModule.retrofit.addReview(
            AddReviewRequest(reviewBody, rating, showId),
            sharedPreferences.getString("access-token", "")!!,
            sharedPreferences.getString("client", "")!!,
            sharedPreferences.getString("uid", "")!!
        )

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
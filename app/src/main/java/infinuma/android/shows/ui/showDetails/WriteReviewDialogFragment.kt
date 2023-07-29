package infinuma.android.shows.ui.showDetails

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.R
import infinuma.android.shows.data.PROFILE_PHOTO_URI
import infinuma.android.shows.databinding.FragmentWriteReviewDialogBinding
import infinuma.android.shows.models.User
import infinuma.android.shows.ui.sharedPreferences
import kotlin.math.roundToInt

class WriteReviewDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentWriteReviewDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var review: ReviewListItem.Review
    private var reviews: MutableList<ReviewListItem> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWriteReviewDialogBinding.inflate(layoutInflater)
        binding.reviewInputField.setHintTextColor(Color.BLACK)
        binding.closeSheet.setOnClickListener {
            this.dismiss()
        }
        binding.reviewRatingInput.setOnRatingBarChangeListener { ratingBar, fl, b ->
            binding.submitReview.isEnabled = true
        }
        binding.submitReview.setOnClickListener {
            review = ReviewListItem.Review(
                getString(R.string.placeholder_review_author),
                binding.reviewInputField.text.toString(),
                binding.reviewRatingInput.rating.toInt(),
                123,
                User(
                    sharedPreferences.getString("id", "0")!!,
                    sharedPreferences.getString("email", "")!!,
                    sharedPreferences.getString(PROFILE_PHOTO_URI, "")!!
                )
            )
            updateRatingBar()

            val intent = Intent("newShowList")
            intent.putExtra("reviewList", bundleOf("reviews" to reviews))
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
            this.dismiss()
        }
        return binding.root
    }

    private fun updateRatingBar() {
        if (reviews.size == 1) {
            reviews.add(1, ReviewListItem.Rating(review.rating.toFloat(), 1))
           // addReview(review.reviewerName, review.reviewText, review.rating, sharedPreferences.getString(PROFILE_PHOTO_URI, "")!!)
        } else {
            //addReview(review.reviewerName, review.reviewText, review.rating, sharedPreferences.getString(PROFILE_PHOTO_URI, "")!!)
            reviews[1] = ReviewListItem.Rating(
                ((((reviews[1].toString()
                    .toFloat() * (reviews.size - 3)) + review.rating.toFloat()) / (reviews.size - 2)) * 100).roundToInt() / 100f,
                reviews.size - 2
            )
        }
    }

    fun getReviews(mutableList: MutableList<ReviewListItem>) {
        reviews = mutableList
    }

    private fun addReview(id: String, review: String, rating: Int, showId: Int, user: User) {
        reviews.add(
            ReviewListItem.Review(
                id,
                review,
                rating,
                showId,
                User(
                    sharedPreferences.getString("id", "0")!!,
                    sharedPreferences.getString("email", "")!!,
                    sharedPreferences.getString(PROFILE_PHOTO_URI, "")!!
                )
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
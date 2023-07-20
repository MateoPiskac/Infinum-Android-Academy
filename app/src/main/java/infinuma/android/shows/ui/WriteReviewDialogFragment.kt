package infinuma.android.shows.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import infinuma.android.shows.R
import infinuma.android.shows.databinding.FragmentWriteReviewDialogBinding

class WriteReviewDialogFragment : BottomSheetDialogFragment(R.layout.fragment_write_review_dialog) {

    private lateinit var binding: FragmentWriteReviewDialogBinding
    private lateinit var review: ReviewListItem.Review
    private var reviewList: MutableList<ReviewListItem> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentWriteReviewDialogBinding.inflate(layoutInflater)

        binding.reviewRatingInput.setOnRatingBarChangeListener { ratingBar, fl, b ->
            binding.submitReview.isEnabled = true
        }
        binding.submitReview.setOnClickListener {
            review = ReviewListItem.Review(
                getString(R.string.placeholder_review_author),
                binding.reviewInputField.text.toString(),
                binding.reviewRatingInput.rating.toInt()
            )
            if (reviewList.size == 1) {
                reviewList.add(1, ReviewListItem.Rating(review.rating.toFloat(), 1))
                addReview(review.reviewerName, review.reviewText, review.rating)
            } else {
                addReview(getString(R.string.placeholder_review_author), getString(R.string.placeholder_review), 3)
                println("BAKI "+review.rating)
                reviewList[1] = ReviewListItem.Rating(
                    ((reviewList[1].toString().toFloat() * (reviewList.size - 3)) + review.rating.toFloat()) / (reviewList.size - 2), reviewList.size - 2
                )
            }
        }


        return binding.root
    }

    fun getReviews(mutableList: MutableList<ReviewListItem>) {
        reviewList = mutableList
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
}
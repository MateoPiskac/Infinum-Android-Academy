package infinuma.android.shows.ui.showDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import infinuma.android.shows.databinding.ReviewListItemBinding
import infinuma.android.shows.databinding.ShowDetailsItemBinding
import infinuma.android.shows.databinding.ShowNoreviewsItemBinding
import infinuma.android.shows.databinding.ShowRatingItemBinding

sealed class ReviewListItem {
    data class Review(val reviewerName: String, val reviewText: String, val rating: Int, val profilePhotoURI: String) : ReviewListItem()
    data class ShowDetails(val showImageResId: Int, val description: String) : ReviewListItem()
    data class Rating(val averageRating: Float, val numberOfReviews: Int) : ReviewListItem() {
        override fun toString(): String {
            return averageRating.toString()
        }
    }

    object NoReviews : ReviewListItem()
}

class ReviewListAdapter : ListAdapter<ReviewListItem, ReviewListAdapter.ShowDetailReviewViewHolder>(DiffCallback()) {
    class DiffCallback : DiffUtil.ItemCallback<ReviewListItem>() {
        override fun areItemsTheSame(oldItem: ReviewListItem, newItem: ReviewListItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ReviewListItem, newItem: ReviewListItem): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ReviewListItem.ShowDetails -> 0
            is ReviewListItem.Review -> 1
            is ReviewListItem.Rating -> 2
            is ReviewListItem.NoReviews -> 3
        }
    }

    sealed class ShowDetailReviewViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        class ReviewViewHolder(private var binding: ReviewListItemBinding) : ShowDetailReviewViewHolder(binding.root) {
            fun bind(review: ReviewListItem.Review) {
                binding.reviewAuthor.text = review.reviewerName
                binding.reviewRating.text = review.rating.toString()
                binding.reviewBody.text = review.reviewText
                //                THIS IS PREPARATION FOR CUSTOM REVIEW PROFILE PHOTOS
                //                Glide.with(adapterContext).load(sharedPreferences.getString(PROFILEPHOTOURI,""))
                //                    .into(binding.reviewAuthorProfilePicture)
            }
        }

        class ShowDetailsViewHolder(private var binding: ShowDetailsItemBinding) : ShowDetailReviewViewHolder(binding.root) {
            fun bind(details: ReviewListItem.ShowDetails) {
                binding.showImage.setImageResource(details.showImageResId)
                binding.showDescription.text = details.description
            }
        }

        class RatingViewHolder(private var binding: ShowRatingItemBinding) : ShowDetailReviewViewHolder(binding.root) {

            fun bind(rating: ReviewListItem.Rating) {
                """${rating.numberOfReviews} REVIEWS ${rating.averageRating} AVERAGE""".also { binding.reviewRatingText.text = it }
                binding.ratingBar.rating = rating.averageRating
            }
        }

        class NoReviewViewHolder(private var binding: ShowNoreviewsItemBinding) : ShowDetailReviewViewHolder(binding.root) {
            fun bind() {
                binding.noReviewsText.isVisible = true
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowDetailReviewViewHolder {
        return when (viewType) {
            0 -> {
                val binding = ShowDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowDetailReviewViewHolder.ShowDetailsViewHolder(binding)
            }

            1 -> {
                val binding = ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowDetailReviewViewHolder.ReviewViewHolder(binding)
            }

            2 -> {
                val binding = ShowRatingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowDetailReviewViewHolder.RatingViewHolder(binding)
            }

            3 -> {
                val binding = ShowNoreviewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowDetailReviewViewHolder.NoReviewViewHolder(binding)
            }

            else -> {
                throw IllegalStateException("Unknown viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: ShowDetailReviewViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ReviewListItem.Review -> (holder as? ShowDetailReviewViewHolder.ReviewViewHolder)?.bind(item)
            is ReviewListItem.ShowDetails -> (holder as? ShowDetailReviewViewHolder.ShowDetailsViewHolder)?.bind(item)
            is ReviewListItem.Rating -> (holder as? ShowDetailReviewViewHolder.RatingViewHolder)?.bind(item)
            is ReviewListItem.NoReviews -> (holder as? ShowDetailReviewViewHolder.NoReviewViewHolder)?.bind()
        }
    }
}
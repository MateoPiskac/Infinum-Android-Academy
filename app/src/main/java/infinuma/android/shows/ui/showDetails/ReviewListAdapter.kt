package infinuma.android.shows.ui.showDetails

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.databinding.ReviewListItemBinding
import infinuma.android.shows.databinding.ShowDetailsItemBinding
import infinuma.android.shows.databinding.ShowNoreviewsItemBinding
import infinuma.android.shows.databinding.ShowRatingItemBinding
import infinuma.android.shows.models.User
import kotlinx.serialization.Serializable

sealed class ReviewListItem {
    data class Review(val id: String, val comment: String, val rating: Int, val showId: Int, val user: User) : ReviewListItem()
    data class ShowDetails(val showImageURI: String, val description: String) : ReviewListItem()
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
        class ReviewViewHolder(private var binding: ReviewListItemBinding, private val context: Context) : ShowDetailReviewViewHolder(binding.root) {
            fun bind(review: ReviewListItem.Review) {
                binding.reviewAuthor.text = review.user.email.substringBefore("@")
                binding.reviewRating.text = review.rating.toString()
                binding.reviewBody.text = review.comment
                Glide.with(context).load(review.user.imageUrl).placeholder(R.drawable.placeholder_profile_picture)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.reviewAuthorProfilePicture)
            }
        }

        class ShowDetailsViewHolder(private var binding: ShowDetailsItemBinding, private val context: Context) :
            ShowDetailReviewViewHolder(binding.root) {
            fun bind(details: ReviewListItem.ShowDetails) {
                Glide.with(context).load(details.showImageURI).placeholder(R.drawable.show_placeholder_icon).centerCrop()
                    .into(binding.showImage)
                Log.e("DETAILS IMAGE", details.showImageURI)
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
                ShowDetailReviewViewHolder.ShowDetailsViewHolder(binding, parent.context)
            }

            1 -> {
                val binding = ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ShowDetailReviewViewHolder.ReviewViewHolder(binding,parent.context)
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
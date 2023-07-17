package infinuma.android.shows.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import infinuma.android.shows.databinding.ReviewListItemBinding
import infinuma.android.shows.databinding.ShowDetailsItemBinding

sealed class ReviewListItem {
    data class Review(val reviewerName: String, val reviewText: String, val rating: Int) : ReviewListItem()
    data class ShowDetails(val showImageResId: Int, val description: String) : ReviewListItem()
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
        }
    }

    sealed class ShowDetailReviewViewHolder(rootView: View) : RecyclerView.ViewHolder(rootView) {
        class ReviewViewHolder(private var binding: ReviewListItemBinding) : ShowDetailReviewViewHolder(binding.root) {
            fun bind(review: ReviewListItem.Review) {
                binding.reviewAuthor.text = review.reviewerName
                binding.reviewRating.text = review.rating.toString()
                binding.reviewBody.text = review.reviewText
            }
        }

        class ShowDetailsViewHolder(private var binding: ShowDetailsItemBinding) : ShowDetailReviewViewHolder(binding.root) {
            fun bind(details: ReviewListItem.ShowDetails) {
                binding.showImage.setImageResource(details.showImageResId)
                binding.showDescription.text = details.description
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

            else -> {
                throw IllegalStateException("Unknown viewType")
            }
        }
    }

    override fun onBindViewHolder(holder: ShowDetailReviewViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is ReviewListItem.Review -> (holder as? ShowDetailReviewViewHolder.ReviewViewHolder)?.bind(item)
            is ReviewListItem.ShowDetails -> (holder as? ShowDetailReviewViewHolder.ShowDetailsViewHolder)?.bind(item)
        }
    }
}
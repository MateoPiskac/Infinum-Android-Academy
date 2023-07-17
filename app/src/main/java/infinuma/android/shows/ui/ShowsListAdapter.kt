package infinuma.android.shows.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.ShowsListItemBinding

class ShowsListAdapter(private val shows: List<Show>, private val onItemClickCallback: (Show) -> Unit) :
    RecyclerView.Adapter<ShowsListAdapter.ShowsViewHolder>() {

    inner class ShowsViewHolder(private val binding: ShowsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.cardView.setOnClickListener {
                onItemClickCallback.invoke(show)
            }
            binding.showTitle.text = show.Title
            binding.showImage.setImageResource(show.Image)
            binding.showDescription.text = show.Description
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ShowsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount(): Int {
        return shows.size
    }
}

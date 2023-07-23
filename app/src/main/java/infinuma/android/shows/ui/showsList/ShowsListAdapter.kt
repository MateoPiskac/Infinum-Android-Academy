package infinuma.android.shows.ui.showsList

import android.view.LayoutInflater
import android.view.ViewGroup
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
            binding.showTitle.text = show.title
            binding.showImage.setImageResource(show.image)
            binding.showDescription.text = show.description
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

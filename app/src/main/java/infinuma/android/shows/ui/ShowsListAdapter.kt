package infinuma.android.shows.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.ShowsListItemBinding

class ShowsListAdapter(private val shows : List<Show>, private val onItemClickCallback : (Show) -> Unit) :
    ListAdapter<Show, ShowsListAdapter.ShowsViewHolder>(DiffUtilCallback()) {

    private class DiffUtilCallback : DiffUtil.ItemCallback<Show>(){
        override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean =
            oldItem.toString()===newItem.toString()

        override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean =
            oldItem==newItem

    }

    class ShowsViewHolder(private val binding : ShowsListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(show: Show) {
            binding.showTitle.text= show.Title
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


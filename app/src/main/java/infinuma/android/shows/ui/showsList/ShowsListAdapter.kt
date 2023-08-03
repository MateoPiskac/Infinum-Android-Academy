package infinuma.android.shows.ui.showsList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import infinuma.android.shows.R
import infinuma.android.shows.data.Show
import infinuma.android.shows.databinding.ShowsListItemBinding

class ShowsListAdapter(private var shows: List<Show>, private val onItemClickCallback: (Show) -> Unit) :
    RecyclerView.Adapter<ShowsListAdapter.ShowsViewHolder>() {

    inner class ShowsViewHolder(private val binding: ShowsListItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(show: Show) {
            binding.cardView.setOnClickListener {
                onItemClickCallback.invoke(show)
            }
            binding.showTitle.text = show.title
            Glide.with(context).load(show.image).centerCrop().placeholder(R.drawable.show_placeholder_icon).into(binding.showImage)
            binding.showDescription.text = show.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsViewHolder {
        val binding = ShowsListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ShowsViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ShowsViewHolder, position: Int) {
        holder.bind(shows[position])
    }

    override fun getItemCount(): Int {
        return shows.size
    }

    fun submitList(newList: List<Show>) {
        shows = newList
    }


}

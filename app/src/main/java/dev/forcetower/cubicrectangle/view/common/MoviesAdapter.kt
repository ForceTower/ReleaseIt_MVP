package dev.forcetower.cubicrectangle.view.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.extensions.inflate
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.ItemMovieBinding

class MoviesAdapter(
    private val view: MovieClickableContract.View
) : PagedListAdapter<Movie, MoviesAdapter.MovieHolder>(
    DiffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(parent.inflate(R.layout.item_movie), view)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        holder.binding.movie = getItem(position)
    }

    inner class MovieHolder(
        val binding: ItemMovieBinding,
        view: MovieClickableContract.View
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.contract = view
        }
    }
    private object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }
}
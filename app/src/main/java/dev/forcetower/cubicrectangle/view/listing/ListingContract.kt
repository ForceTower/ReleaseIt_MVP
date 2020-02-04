package dev.forcetower.cubicrectangle.view.listing

import androidx.annotation.StringRes
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.core.model.database.Movie
import kotlinx.coroutines.CoroutineScope

class ListingContract {
    interface View : BaseContract.View {
        fun getLifecycleScope(): CoroutineScope
        fun onMovieClick(movie: Movie)
        fun onLoadError(@StringRes resource: Int)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadMoviesByGenre(genreId: Long): PagedList<Movie>
    }
}
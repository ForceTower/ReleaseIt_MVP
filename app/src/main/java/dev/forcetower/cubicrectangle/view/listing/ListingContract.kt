package dev.forcetower.cubicrectangle.view.listing

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.view.common.MovieClickableContract
import kotlinx.coroutines.CoroutineScope

interface ListingContract {
    interface View : MovieClickableContract.View {
        fun getLifecycleScope(): CoroutineScope
        fun onLoadError(@StringRes resource: Int) {
            println("called")
        }
        fun moveToErrorState()
        fun moveToLoadingState()
        fun moveToListingState()
        fun isRefreshing(refreshing: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        val listing: LiveData<PagedList<Movie>>
        fun loadMoviesByGenre(genreId: Long, scope: CoroutineScope? = null)
        fun retry()
        fun refresh()
    }
}
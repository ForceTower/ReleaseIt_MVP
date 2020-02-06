package dev.forcetower.cubicrectangle.view.listing

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.NetworkState
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.view.common.MovieClickableContract
import kotlinx.coroutines.CoroutineScope

interface ListingContract {
    interface View : MovieClickableContract.View {
        fun getLifecycleScope(): CoroutineScope
        fun onLoadError(@StringRes resource: Int)
        fun moveToErrorState()
        fun moveToLoadingState()
        fun moveToListingState()
    }

    interface Presenter : BaseContract.Presenter<View> {
        val currentState: LiveData<NetworkState>
        fun loadMoviesByGenre(genreId: Long): PagedList<Movie>
        fun retry()
        fun refresh()
    }
}
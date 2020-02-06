package dev.forcetower.cubicrectangle.view.search

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.view.common.MovieClickableContract
import kotlinx.coroutines.CoroutineScope

interface SearchContract {
    interface View : MovieClickableContract.View {
        fun getLifecycleScope(): CoroutineScope
        fun onLoadError(@StringRes resource: Int)
        fun onNavigateBack()
        fun onClearSearch()
        fun moveToErrorState()
        fun moveToLoadingState()
        fun moveToListingState()
        fun isRefreshing(refreshing: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        val listing: LiveData<PagedList<Movie>>
        fun search(query: String, scope: CoroutineScope? = null)
        fun retry()
        fun refresh()
    }
}
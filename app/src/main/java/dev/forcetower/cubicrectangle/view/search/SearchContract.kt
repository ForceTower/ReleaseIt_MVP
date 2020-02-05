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
    }

    interface Presenter : BaseContract.Presenter<View> {
        val searchSource: LiveData<PagedList<Movie>>
        fun search(query: String, scope: CoroutineScope? = null)
    }
}
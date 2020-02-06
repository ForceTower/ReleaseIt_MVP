package dev.forcetower.cubicrectangle.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope

class SearchPresenter(
    private val repository: MoviesRepository
) : SearchContract.Presenter {
    private var view: SearchContract.View? = null

    override val searchSource = MediatorLiveData<PagedList<Movie>>()
//    override val searchSource: LiveData<PagedList<Movie>>
//        get() = _searchSource

    override fun search(query: String, scope: CoroutineScope?) {
        if (query.isBlank()) {
            searchSource.value = repository.emptySource()
        } else {
            searchSource.value = repository.search(query, scope ?: view!!.getLifecycleScope()) {
                view?.onLoadError(R.string.network_error)
            }
        }
    }

    override fun attach(v: SearchContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }
}
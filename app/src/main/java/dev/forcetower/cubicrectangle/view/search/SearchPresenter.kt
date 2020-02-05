package dev.forcetower.cubicrectangle.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository

class SearchPresenter(
    private val repository: MoviesRepository
) : SearchContract.Presenter {
    private var view: SearchContract.View? = null

    private val _searchSource = MediatorLiveData<PagedList<Movie>>()
    override val searchSource: LiveData<PagedList<Movie>>
        get() = _searchSource

    override fun search(query: String) {
        _searchSource.value = repository.search(query, view!!.getLifecycleScope())
    }

    override fun attach(v: SearchContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }
}
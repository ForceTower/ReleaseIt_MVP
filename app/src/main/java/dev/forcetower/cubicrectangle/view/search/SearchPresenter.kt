package dev.forcetower.cubicrectangle.view.search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.switchMap
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Status
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.CoroutineScope

class SearchPresenter(
    private val repository: MoviesRepository
) : SearchContract.Presenter {
    private var view: SearchContract.View? = null
    private val _searchSource = MediatorLiveData<Listing<Movie>>()

    override val listing = _searchSource.switchMap { it.pagedList }
    private val refreshState = _searchSource.switchMap { it.refreshState }
    private val networkState = _searchSource.switchMap { it.networkState }

    private var lastSearch = ""

    init {
        _searchSource.addSource(networkState) { change ->
            change ?: return@addSource
            if (change.status == Status.FAILED && listing.value.isNullOrEmpty()) {
                view?.moveToErrorState()
            } else if (change.status == Status.RUNNING && listing.value.isNullOrEmpty()) {
                view?.moveToLoadingState()
            } else {
                view?.moveToListingState()
            }
            Unit
        }
        _searchSource.addSource(refreshState) { change ->
            if (change?.status == Status.RUNNING) {
                view?.isRefreshing(true)
            } else {
                view?.isRefreshing(false)
            }
        }
    }

    override fun search(query: String, scope: CoroutineScope?) {
        if (lastSearch == query) return
        lastSearch = query
        if (query.isBlank()) {
            _searchSource.value = repository.emptySource2()
        } else {
            _searchSource.value = repository.query(query, scope ?: view!!.getLifecycleScope()) {
                view?.onLoadError(R.string.network_error)
            }
        }
    }

    override fun retry() {
        _searchSource.value?.retry?.invoke()
    }

    override fun refresh() {
        _searchSource.value?.refresh?.invoke()
    }

    override fun attach(v: SearchContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }
}
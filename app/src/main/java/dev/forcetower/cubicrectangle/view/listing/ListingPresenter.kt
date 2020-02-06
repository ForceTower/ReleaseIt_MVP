package dev.forcetower.cubicrectangle.view.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Status

class ListingPresenter constructor(
    private val repository: MoviesRepository
) : ListingContract.Presenter {
    private var view: ListingContract.View? = null
    private val _genreSource = MediatorLiveData<Listing<Movie>>()

    override val listing: LiveData<PagedList<Movie>> = _genreSource.switchMap { it.pagedList }
    private val refreshState = _genreSource.switchMap { it.refreshState }
    private val networkState = _genreSource.switchMap { it.networkState }

    private var lastGenre = 0L

    init {
        _genreSource.addSource(networkState) { change ->
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
        _genreSource.addSource(refreshState) { change ->
            if (change?.status == Status.RUNNING) {
                view?.isRefreshing(true)
            } else {
                view?.isRefreshing(false)
            }
        }
    }

    override fun attach(v: ListingContract.View) {
        view = v
    }

    override fun retry() {
        _genreSource.value?.retry?.invoke()
    }

    override fun refresh() {
        _genreSource.value?.refresh?.invoke()
    }

    override fun loadMoviesByGenre(genreId: Long) {
        if (lastGenre == genreId) return
        lastGenre = genreId
        _genreSource.value = repository.queryMoviesByGenre(genreId, view!!.getLifecycleScope()) {
            // view?.onLoadError(R.string.network_error)
        }
    }

    override fun onDestroy() {
        view = null
    }
}
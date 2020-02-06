package dev.forcetower.cubicrectangle.view.listing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.NetworkState
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Status

class ListingPresenter constructor(
    private val repository: MoviesRepository
) : ListingContract.Presenter {
    private var view: ListingContract.View? = null
    private var source: Listing<Movie>? = null

    private var retry: (() -> Unit)? = null
    private var refresh: (() -> Unit)? = null

    private val _currentState = MediatorLiveData<NetworkState>()
    override val currentState: LiveData<NetworkState>
        get() = _currentState

    private var networkSource: LiveData<NetworkState>? = null

    override fun attach(v: ListingContract.View) {
        view = v
    }

    override fun retry() {
        retry?.invoke()
    }

    override fun refresh() {
        refresh?.invoke()
    }

    override fun loadMoviesByGenre(genreId: Long): PagedList<Movie> {
        val src = source
        return if (src == null) {
            val new = repository.queryMoviesByGenre(genreId, view!!.getLifecycleScope()) {
                // view?.onLoadError(R.string.network_error)
            }
            source = new

            retry = new.retry
            refresh = new.refresh
            networkSource = new.networkState
            observeNetwork()
            new.pagedList
        } else {
            observeNetwork()
            src.pagedList
        }
    }

    private fun observeNetwork() {
        networkSource?.let {
            _currentState.removeSource(it)
            _currentState.addSource(it) { change ->
                if (change.status == Status.FAILED && source?.pagedList.isNullOrEmpty()) {
                    view?.moveToErrorState()
                    view?.onLoadError(R.string.network_error)
                } else if (change.status == Status.RUNNING && source?.pagedList.isNullOrEmpty()) {
                    view?.moveToLoadingState()
                } else {
                    view?.moveToListingState()
                }
                _currentState.value = change
                Unit
            }
        }
    }

    override fun onDestroy() {
        view = null
    }
}
package dev.forcetower.cubicrectangle.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.NetworkState
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Status
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.CoroutineScope

class SearchPresenter(
    private val repository: MoviesRepository
) : SearchContract.Presenter {
    private var view: SearchContract.View? = null

    private val list = MediatorLiveData<PagedList<Movie>>()
    override val listing: LiveData<PagedList<Movie>>
        get() = list

    private val _searchSource = MediatorLiveData<Listing<Movie>>()

    private var retry: (() -> Unit)? = null
    private var refresh: (() -> Unit)? = null

    private var currentState: LiveData<NetworkState>? = null

    init {
        list.addSource(_searchSource) {
            list.value = it.pagedList
            retry = it.retry
            refresh = it.refresh

            currentState?.let { current -> list.removeSource(current) }
            val state = it.networkState
            if (state == null) {
                view?.moveToListingState()
            } else {
                currentState = state
                list.addSource(state) { change ->
                    if (change.status == Status.FAILED && it.pagedList.isEmpty()) {
                        view?.moveToErrorState()
                    } else if (change.status == Status.RUNNING && it.pagedList.isEmpty()) {
                        view?.moveToLoadingState()
                    } else {
                        view?.moveToListingState()
                    }
                    Unit
                }
            }
        }
    }

    override fun search(query: String, scope: CoroutineScope?) {
        if (query.isBlank()) {
            _searchSource.value = repository.emptySource()
        } else {
            _searchSource.value = repository.query(query, scope ?: view!!.getLifecycleScope()) {
                view?.onLoadError(R.string.network_error)
            }
        }
    }

    override fun retry() {
        retry?.invoke()
    }

    override fun refresh() {
        refresh?.invoke()
    }

    override fun attach(v: SearchContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }
}
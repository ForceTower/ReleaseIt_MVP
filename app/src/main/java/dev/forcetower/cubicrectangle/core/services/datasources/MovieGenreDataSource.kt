package dev.forcetower.cubicrectangle.core.services.datasources

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.toMovie
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.Executor

class MovieGenreDataSource(
    genres: List<Long>,
    private val service: TMDbService,
    private val scope: CoroutineScope,
    private val error: (Throwable) -> Unit,
    private val retryExecutor: Executor
) : PageKeyedDataSource<Int, Movie>() {
    private val genreString = genres.joinToString(",")

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        scope.launch {
            try {
                networkState.postValue(NetworkState.LOADING)
                val results = service.moviesByGenre(genreString).results
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(results.map { it.toMovie() }, null, 2)
            } catch (t: Throwable) {
                error(t)
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error(t.message ?: "unknown error")
                networkState.postValue(error)
                initialLoad.postValue(error)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                networkState.postValue(NetworkState.LOADING)
                val response = service.moviesByGenre(genreString, params.key)
                retry = null
                networkState.postValue(NetworkState.LOADED)
                val nextPage = if (response.page >= response.totalPages) null else response.page + 1
                callback.onResult(response.results.map { it.toMovie() }, nextPage)
            } catch (t: Throwable) {
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                error(t)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                networkState.postValue(NetworkState.LOADING)
                val response = service.moviesByGenre(genreString, params.key)
                retry = null
                networkState.postValue(NetworkState.LOADED)
                val previousPage = if (response.page == 1) null else response.page - 1
                callback.onResult(response.results.map { it.toMovie() }, previousPage)
            } catch (t: Throwable) {
                retry = {
                    loadBefore(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                error(t)
            }
        }
    }

    override fun invalidate() {
        Timber.d("Invalidated data source")
        super.invalidate()
    }
}
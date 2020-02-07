package dev.forcetower.cubicrectangle.core.services.datasources

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.NetworkState
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.toMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor

class QueryDataSource(
    private val query: String,
    private val service: TMDbService,
    private val scope: CoroutineScope,
    private val error: (Throwable) -> Unit,
    private val retryExecutor: Executor,
    private val database: ReleaseDB
) : PageKeyedDataSource<Int, Movie>() {
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
                val results = service.searchMovie(query).results
                retry = null
                networkState.postValue(NetworkState.LOADED)
                initialLoad.postValue(NetworkState.LOADED)
                callback.onResult(results.map { it.toMovie() }, null, 2)
                database.movies().insertSimpleList(results)
            } catch (t: Throwable) {
                Timber.i(t, "Failed loading movies by genre")
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
                val response = service.searchMovie(query, params.key)
                val nextPage = if (response.page >= response.totalPages) null else response.page + 1
                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(response.results.map { it.toMovie() }, nextPage)
                withContext(Dispatchers.IO) {
                    database.movies().insertSimpleList(response.results)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                Timber.i(t, "Failed loading movies by genre")
                error(t)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                networkState.postValue(NetworkState.LOADING)
                val response = service.searchMovie(query, params.key)
                val previousPage = if (response.page == 1) null else response.page - 1
                retry = null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(response.results.map { it.toMovie() }, previousPage)
                withContext(Dispatchers.IO) {
                    database.movies().insertSimpleList(response.results)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
                retry = {
                    loadBefore(params, callback)
                }
                networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                Timber.i(t, "Failed loading movies by genre")
                error(t)
            }
        }
    }

    override fun invalidate() {
        Timber.d("Invalidated data source")
        super.invalidate()
    }
}
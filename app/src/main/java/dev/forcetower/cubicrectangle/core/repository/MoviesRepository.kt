package dev.forcetower.cubicrectangle.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.AppExecutors
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.EmptyDataSource
import dev.forcetower.cubicrectangle.core.services.datasources.MovieGenreDataSource
import dev.forcetower.cubicrectangle.core.services.datasources.QueryDataSource
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.toMovie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRepository @Inject constructor(
    private val service: TMDbService,
    private val executors: AppExecutors
) {

    fun getMovies(): LiveData<List<Movie>> {
        return liveData(Dispatchers.IO) {
            try {
                // val genres = service.genres().genres
                val results = service.moviesPopular(1).results
                emit(results.map { it.toMovie() })
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun searchLive(query: String): LiveData<List<Movie>> {
        return liveData(Dispatchers.IO) {
            try {
                val result = service.searchMovie(query).results
                emit(result.map { it.toMovie() })
                Timber.d("Result $result")
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun getMovie(
        movieId: Long,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): LiveData<Movie> {
        return liveData(Dispatchers.IO + scope.coroutineContext) {
            try {
                emit(service.movieDetails(movieId).toMovie())
            } catch (t: Throwable) {
                Timber.i(t)
                error(t)
            }
        }
    }

    @Deprecated("This method is simpler, but doesn't offer error handle")
    fun search(
        query: String,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): PagedList<Movie> {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .build()

        val source = QueryDataSource(query, service, scope, error, executors.networkIO())
        return PagedList.Builder(source, config)
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.networkIO())
            .build()
    }

    fun query(
        query: String,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): Listing<Movie> {
        val source = QueryDataSource(query, service, scope, error, executors.networkIO())
        val list = PagedList.Builder(source, getDefaultConfig())
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.networkIO())
            .build()

        val refreshState = source.initialLoad
        return Listing(
            pagedList = list,
            networkState = source.networkState,
            retry = { source.retryAllFailed() },
            refresh = { source.invalidate() },
            refreshState = refreshState
        )
    }

    // This is network only. TODO create a database + network
    fun getMoviesByGenre(
        genre: Long,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): PagedList<Movie> {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .build()

        val source = MovieGenreDataSource(listOf(genre), service, scope, error, executors.networkIO())
        return PagedList.Builder(source, config)
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.diskIO())
            .build()
    }

    fun queryMoviesByGenre(
        genre: Long,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): Listing<Movie> {
        val config = getDefaultConfig()

        val source = MovieGenreDataSource(listOf(genre), service, scope, error, executors.networkIO())
        val list = PagedList.Builder(source, config)
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.diskIO())
            .build()

        return Listing(
            pagedList = list,
            networkState = source.networkState,
            retry = { source.retryAllFailed() },
            refresh = { source.invalidate() },
            refreshState = source.initialLoad
        )
    }

    fun <T> emptySource(): Listing<T> {
        val source = EmptyDataSource<T>()
        val list = PagedList.Builder(source, getDefaultConfig())
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.networkIO())
            .build()

        return Listing(
            pagedList = list,
            networkState = null,
            retry = {},
            refresh = {},
            refreshState = null
        )
    }

    private fun getDefaultConfig() = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(20)
        .build()
}
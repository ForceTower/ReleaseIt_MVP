package dev.forcetower.cubicrectangle.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import androidx.paging.toLiveData
import dev.forcetower.cubicrectangle.AppExecutors
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.MovieGenreDataSource
import dev.forcetower.cubicrectangle.core.services.datasources.factory.MovieGenreDataSourceFactory
import dev.forcetower.cubicrectangle.core.services.datasources.QueryDataSource
import dev.forcetower.cubicrectangle.core.services.datasources.factory.QueryDataSourceFactory
import dev.forcetower.cubicrectangle.core.services.datasources.helpers.Listing
import dev.forcetower.cubicrectangle.core.ui.lifecycle.EmptyLiveData
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

    fun query(
        query: String,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): Listing<Movie> {
        val factory = QueryDataSourceFactory(query, service, scope, error, executors.networkIO())
        val livePagedList = factory.toLiveData(
            pageSize = 20,
            fetchExecutor = executors.networkIO()
        )
        val refreshState = factory.sourceLiveData.switchMap {
            it.initialLoad
        }

        return Listing(
            pagedList = livePagedList,
            networkState = factory.sourceLiveData.switchMap {
                it.networkState
            },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }

    fun queryMoviesByGenre(
        genre: Long,
        scope: CoroutineScope,
        error: (Throwable) -> Unit
    ): Listing<Movie> {
        val factory = MovieGenreDataSourceFactory(
            listOf(genre),
            service,
            scope,
            error,
            executors.networkIO()
        )

        val livePagedList = factory.toLiveData(
            pageSize = 20,
            fetchExecutor = executors.networkIO()
        )

        val refreshState = factory.sourceLiveData.switchMap {
            it.initialLoad
        }

        return Listing(
            pagedList = livePagedList,
            networkState = factory.sourceLiveData.switchMap { it.networkState },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            refreshState = refreshState
        )
    }

    fun <T> emptySource(): Listing<T> {
        return Listing(
            pagedList = EmptyLiveData.create(),
            networkState = EmptyLiveData.create(),
            retry = {},
            refresh = {},
            refreshState = EmptyLiveData.create()
        )
    }

    @Deprecated("This method is simpler, but doesn't offer error handle / refresh")
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

    @Deprecated("This method is simpler, but doesn't offer error handle / refresh")
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
}
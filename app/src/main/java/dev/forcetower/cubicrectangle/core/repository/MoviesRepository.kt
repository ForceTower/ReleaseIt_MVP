package dev.forcetower.cubicrectangle.core.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.AppExecutors
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.core.model.database.toMovie
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.MovieGenreDataSource
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

    fun getMovie(movieId: Long): LiveData<Movie> {
        return liveData(Dispatchers.IO) {
            try {
                emit(service.movieDetails(movieId).toMovie())
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    fun search(query: String): LiveData<List<Movie>> {
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

    // This is network only. TODO create a database + network
    fun getMoviesByGenre(genre: Long, scope: CoroutineScope): PagedList<Movie> {
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(20)
            .build()

        val source = MovieGenreDataSource(listOf(genre), service, scope)
        return PagedList.Builder(source, config)
            .setNotifyExecutor(executors.mainThread())
            .setFetchExecutor(executors.diskIO())
            .build()
    }
}
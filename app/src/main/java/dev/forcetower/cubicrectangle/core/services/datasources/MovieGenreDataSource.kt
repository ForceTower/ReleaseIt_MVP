package dev.forcetower.cubicrectangle.core.services.datasources

import androidx.paging.PageKeyedDataSource
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.model.database.toMovie
import dev.forcetower.cubicrectangle.core.services.TMDbService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

class MovieGenreDataSource(
    genres: List<Long>,
    private val service: TMDbService,
    private val scope: CoroutineScope,
    private val error: (Throwable) -> Unit
) : PageKeyedDataSource<Int, Movie>() {
    private val genreString = genres.joinToString(",")

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        scope.launch {
            try {
                val results = service.moviesByGenre(genreString).results
                callback.onResult(results.map { it.toMovie() }, null, 2)
            } catch (t: Throwable) {
                Timber.i(t, "Failed loading movies by genre")
                error(t)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                val response = service.moviesByGenre(genreString, params.key)
                val nextPage = if (response.page >= response.totalPages) null else response.page + 1
                callback.onResult(response.results.map { it.toMovie() }, nextPage)
            } catch (t: Throwable) {
                Timber.i(t, "Failed loading movies by genre")
                callback.onError(t)
                error(t)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        scope.launch {
            try {
                val response = service.moviesByGenre(genreString, params.key)
                val previousPage = if (response.page == 1) null else response.page - 1
                callback.onResult(response.results.map { it.toMovie() }, previousPage)
            } catch (t: Throwable) {
                Timber.i(t, "Failed loading movies by genre")
                error(t)
            }
        }
    }

    override fun invalidate() {
        Timber.d("Invalidated data source")
        super.invalidate()
        scope.cancel("DataSource invalidated")
    }
}
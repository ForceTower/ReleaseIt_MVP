package dev.forcetower.cubicrectangle.core.services.datasources.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.MovieGenreDataSource
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executor

class MovieGenreDataSourceFactory(
    private val genres: List<Long>,
    private val service: TMDbService,
    private val scope: CoroutineScope,
    private val error: (Throwable) -> Unit,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, Movie>() {
    val sourceLiveData = MutableLiveData<MovieGenreDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val source = MovieGenreDataSource(genres, service, scope, error, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
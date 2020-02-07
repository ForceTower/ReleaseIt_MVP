package dev.forcetower.cubicrectangle.core.services.datasources.factory

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import dev.forcetower.cubicrectangle.core.persistence.ReleaseDB
import dev.forcetower.cubicrectangle.core.services.TMDbService
import dev.forcetower.cubicrectangle.core.services.datasources.QueryDataSource
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executor

/**
 * A simple data source factory which also provides a way to observe the last created data source.
 * This allows us to channel its network request status etc back to the UI. See the Listing creation
 * in the Repository class.
 */
class QueryDataSourceFactory(
    private val query: String,
    private val service: TMDbService,
    private val scope: CoroutineScope,
    private val error: (Throwable) -> Unit,
    private val retryExecutor: Executor,
    private val database: ReleaseDB
) : DataSource.Factory<Int, Movie>() {
    val sourceLiveData = MutableLiveData<QueryDataSource>()
    override fun create(): DataSource<Int, Movie> {
        val source = QueryDataSource(query, service, scope, error, retryExecutor, database)
        sourceLiveData.postValue(source)
        return source
    }
}
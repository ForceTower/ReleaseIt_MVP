package dev.forcetower.cubicrectangle.core.services.datasources.factory

import androidx.paging.DataSource
import dev.forcetower.cubicrectangle.core.services.datasources.EmptyDataSource

class EmptyDataSourceFactory<T> : DataSource.Factory<Int, T>() {
    override fun create(): DataSource<Int, T> {
        return EmptyDataSource()
    }
}
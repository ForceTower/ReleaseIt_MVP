package dev.forcetower.cubicrectangle.core.services.datasources

import androidx.paging.PageKeyedDataSource

class EmptyDataSource<T> : PageKeyedDataSource<Int, T>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        callback.onResult(emptyList(), null, null)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        callback.onResult(emptyList(), null)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        callback.onResult(emptyList(), null)
    }
}
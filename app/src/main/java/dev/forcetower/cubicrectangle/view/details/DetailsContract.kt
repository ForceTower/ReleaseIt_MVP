package dev.forcetower.cubicrectangle.view.details

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import dev.forcetower.cubicrectangle.core.base.BaseContract
import dev.forcetower.cubicrectangle.model.database.Movie
import kotlinx.coroutines.CoroutineScope

interface DetailsContract {
    interface View : BaseContract.View {
        fun getLifecycleScope(): CoroutineScope
        fun onLoadError(@StringRes resource: Int)
        fun onNavigateBack()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadMovieDetails(movieId: Long, scope: CoroutineScope? = null): LiveData<Movie>
    }
}
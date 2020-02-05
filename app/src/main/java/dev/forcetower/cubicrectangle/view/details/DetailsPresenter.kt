package dev.forcetower.cubicrectangle.view.details

import androidx.lifecycle.LiveData
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import dev.forcetower.cubicrectangle.model.database.Movie

class DetailsPresenter constructor(
    private val repository: MoviesRepository
) : DetailsContract.Presenter {
    private var view: DetailsContract.View? = null
    private var source: LiveData<Movie>? = null

    override fun loadMovieDetails(movieId: Long): LiveData<Movie> {
        val src = source ?: repository.getMovie(movieId, view!!.getLifecycleScope()) {
            view?.onLoadError(R.string.network_error)
        }
        source = src
        return src
    }

    override fun attach(v: DetailsContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }
}
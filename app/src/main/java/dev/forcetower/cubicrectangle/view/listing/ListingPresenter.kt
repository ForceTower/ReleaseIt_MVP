package dev.forcetower.cubicrectangle.view.listing

import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository

class ListingPresenter constructor(
    private val repository: MoviesRepository
) : ListingContract.Presenter {
    private var view: ListingContract.View? = null
    private var source: PagedList<Movie>? = null

    override fun attach(v: ListingContract.View) {
        view = v
    }

    override fun onDestroy() {
        view = null
    }

    override fun loadMoviesByGenre(genreId: Long): PagedList<Movie> {
        val src = source ?: repository.getMoviesByGenre(genreId, view!!.getLifecycleScope()) {
            view?.onLoadError(R.string.network_error)
        }
        source = src
        return src
    }
}
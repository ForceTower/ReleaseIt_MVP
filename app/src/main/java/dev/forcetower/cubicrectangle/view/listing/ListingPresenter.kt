package dev.forcetower.cubicrectangle.view.listing

import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import javax.inject.Inject

class ListingPresenter @Inject constructor(
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
        val src = source ?: repository.getMoviesByGenre(genreId, view!!.getLifecycleScope())
        source = src
        return src
    }
}
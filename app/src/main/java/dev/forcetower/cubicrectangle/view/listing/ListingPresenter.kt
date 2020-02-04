package dev.forcetower.cubicrectangle.view.listing

import androidx.paging.PagedList
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.core.repository.MoviesRepository
import javax.inject.Inject

class ListingPresenter @Inject constructor(
    private val repository: MoviesRepository
): ListingContract.Presenter {
    private lateinit var view: ListingContract.View

    override fun attach(v: ListingContract.View) {
        view = v
    }

    override fun loadMoviesByGenre(genreId: Long): PagedList<Movie> {
        return repository.getMoviesByGenre(genreId, view.getLifecycleScope())
    }
}
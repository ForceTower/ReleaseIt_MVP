package dev.forcetower.cubicrectangle.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.FragmentListingBinding
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

class BaseGenreFragment : BaseFragment(), ListingContract.View {
    @Inject
    lateinit var presenter: ListingContract.Presenter
    private lateinit var binding: FragmentListingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.attach(this)
        return FragmentListingBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoviesAdapter()
        binding.apply {
            recyclerMovies.adapter = adapter
        }
        adapter.submitList(presenter.loadMoviesByGenre(12))
    }

    override fun getLifecycleScope(): CoroutineScope {
        return lifecycleScope
    }

    override fun onMovieClick(movie: Movie) {
        Timber.d("Movie clicked $movie")
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource))
    }
}
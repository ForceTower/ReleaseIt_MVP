package dev.forcetower.cubicrectangle.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.FragmentListingBinding
import dev.forcetower.cubicrectangle.view.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

class GenreListingFragment : BaseFragment(), ListingContract.View {
//    I moved the presenter to the viewModel so it's instance state is preserved
//    across configuration changes, but, uncommenting the code below and the
//    getLifecycleScope return, all should still fine
//
//    @Inject
//    lateinit var presenter: ListingContract.Presenter

    @Inject
    lateinit var factory: BaseViewModelFactory

    private lateinit var binding: FragmentListingBinding
    private val viewModel by viewModels<HomeViewModel> { factory }
    private val presenter: ListingContract.Presenter
        get() = viewModel.listingPresenter

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
        val adapter = MoviesAdapter(this)
        binding.apply {
            recyclerMovies.adapter = adapter
        }
        adapter.submitList(presenter.loadMoviesByGenre(12))
    }

    override fun getLifecycleScope(): CoroutineScope {
//        return lifecycleScope
        return viewModel.viewModelScope
    }

    override fun onMovieClick(movie: Movie) {
        Timber.d("Movie clicked $movie")
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource))
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
package dev.forcetower.cubicrectangle.view.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.FragmentListingBinding
import dev.forcetower.cubicrectangle.view.common.MoviesAdapter
import dev.forcetower.cubicrectangle.view.genres.GenresFragmentDirections
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ListingFragment : BaseFragment(), ListingContract.View {
//    I moved the presenter to the viewModel so it's instance state is preserved
//    across configuration changes, but, uncommenting the code below and the
//    getLifecycleScope return, all should still fine
//
//    @Inject
//    lateinit var presenter: ListingContract.Presenter

    @Inject
    lateinit var factory: BaseViewModelFactory

    private lateinit var binding: FragmentListingBinding
    private val viewModel by viewModels<ListingViewModel> { factory }
    private val presenter: ListingContract.Presenter
        get() = viewModel.presenter

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
        presenter.currentState.observe(viewLifecycleOwner, Observer { Unit })
        binding.labelError.setOnClickListener {
            presenter.retry()
        }
        adapter.submitList(presenter.loadMoviesByGenre(requireArguments().getLong("genre_id")))
    }

    override fun getLifecycleScope(): CoroutineScope {
//        return lifecycleScope
        return viewModel.viewModelScope
    }

    override fun onMovieClick(movie: Movie) {
        val direction = GenresFragmentDirections.actionGenresToDetails(movie.id)
        findNavController().navigate(direction)
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource))
    }

    override fun moveToErrorState() {
        binding.labelError.visibility = View.VISIBLE
        binding.labelLoading.visibility = View.GONE
        binding.recyclerMovies.visibility = View.GONE
    }

    override fun moveToLoadingState() {
        binding.labelError.visibility = View.GONE
        binding.labelLoading.visibility = View.VISIBLE
        binding.recyclerMovies.visibility = View.GONE
    }

    override fun moveToListingState() {
        binding.labelError.visibility = View.GONE
        binding.labelLoading.visibility = View.GONE
        binding.recyclerMovies.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun shouldApplyInsets() = false

    companion object {
        fun newInstance(genreId: Long): ListingFragment {
            return ListingFragment()
                .apply {
                    arguments = bundleOf("genre_id" to genreId)
                }
        }
    }
}
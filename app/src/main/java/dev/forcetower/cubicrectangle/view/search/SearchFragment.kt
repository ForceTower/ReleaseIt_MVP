package dev.forcetower.cubicrectangle.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.forEach
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.extensions.requestApplyInsetsWhenAttached
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.FragmentSearchBinding
import dev.forcetower.cubicrectangle.view.common.MoviesAdapter
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class SearchFragment : BaseFragment(), SearchContract.View {
    @Inject
    lateinit var factory: BaseViewModelFactory
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels { factory }
    private val presenter: SearchContract.Presenter
        get() = viewModel.presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.attach(this)
        return FragmentSearchBinding.inflate(inflater, container, false).also {
            binding = it
        }.apply {
            contract = this@SearchFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoviesAdapter(this)
        binding.apply {
            recyclerMovies.adapter = adapter
        }

        presenter.listing.observe(viewLifecycleOwner, Observer { listing ->
            adapter.submitList(listing)
        })

        binding.editQuery.doAfterTextChanged { text ->
            presenter.search(text.toString())
        }

        binding.labelError.setOnClickListener {
            presenter.retry()
        }

        binding.swipe.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onStart() {
        super.onStart()
        view?.requestApplyInsetsWhenAttached()
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onMovieClick(movie: Movie) {
        val direction = SearchFragmentDirections.actionSearchToDetails(movie.id)
        val view = findViewForTransition(binding.recyclerMovies, movie.id)
        view.transitionName = getString(R.string.title_poster_transition, movie.id)
        val extras = FragmentNavigatorExtras(view to getString(R.string.title_poster_transition, movie.id))

        findNavController().navigate(direction, extras)
    }

    private fun findViewForTransition(group: ViewGroup, id: Long): View {
        group.forEach {
            if (it.getTag(R.id.movie_id_tag) == id) {
                return it.findViewById(R.id.image_poster)
            }
        }
        return group
    }

    override fun onLoadError(@StringRes resource: Int) {
        getSnack(getString(resource), Snackbar.LENGTH_LONG)?.setAction(R.string.retry) {
            presenter.retry()
        }?.show()
    }

    override fun onNavigateBack() {
        findNavController().popBackStack()
    }

    override fun onClearSearch() {
        binding.editQuery.setText("")
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

    override fun isRefreshing(refreshing: Boolean) {
        binding.swipe.isRefreshing = refreshing
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun shouldApplyInsets() = false
}
package dev.forcetower.cubicrectangle.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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

        presenter.searchSource.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.editQuery.doAfterTextChanged { text ->
            presenter.search(text.toString())
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
        findNavController().navigate(direction)
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource), Snackbar.LENGTH_LONG)
    }

    override fun onNavigateBack() {
        findNavController().popBackStack()
    }

    override fun onClearSearch() {
        binding.editQuery.setText("")
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun shouldApplyInsets() = false
}
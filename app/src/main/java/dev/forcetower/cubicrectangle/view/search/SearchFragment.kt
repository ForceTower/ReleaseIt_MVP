package dev.forcetower.cubicrectangle.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.extensions.doOnApplyWindowInsets
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.databinding.FragmentSearchBinding
import dev.forcetower.cubicrectangle.view.common.MoviesAdapter
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
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

        binding.appBar.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        binding.recyclerMovies.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        presenter.searchSource.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.editQuery.doAfterTextChanged { text ->
            presenter.search(text.toString())
        }
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onMovieClick(movie: Movie) {
        Timber.d("Movie click ${movie.title}")
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

    override fun shouldApplyBottomInsets() = false
    override fun shouldApplyTopInsets() = false
}
package dev.forcetower.cubicrectangle.view.search

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.model.database.Movie
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

        if (savedInstanceState == null) {
            Handler().postDelayed({
                presenter.search("marvel")
            }, 1000)

            Handler().postDelayed({
                presenter.search("dc")
            }, 3000)
        }
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onMovieClick(movie: Movie) {
        Timber.d("Movie click ${movie.title}")
    }

    override fun onLoadError(resource: Int) {
    }
}
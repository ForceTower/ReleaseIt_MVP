package dev.forcetower.cubicrectangle.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.databinding.FragmentDetailsBinding
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DetailsFragment : BaseFragment(), DetailsContract.View {
    @Inject
    lateinit var factory: BaseViewModelFactory

    private lateinit var binding: FragmentDetailsBinding
    // private val args: DetailsFragmentArgs by navArgs()
    private val viewModel by viewModels<DetailsViewModel> { factory }
    private val presenter: DetailsContract.Presenter
        get() = viewModel.presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.attach(this)
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        presenter.loadMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
//        })
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource), Snackbar.LENGTH_LONG)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
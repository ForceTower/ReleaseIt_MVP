package dev.forcetower.cubicrectangle.view.details

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.ViewCompat.requestApplyInsets
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.extensions.doOnApplyWindowInsets
import dev.forcetower.cubicrectangle.core.extensions.fadeIn
import dev.forcetower.cubicrectangle.databinding.FragmentDetailsBinding
import dev.forcetower.cubicrectangle.model.database.Movie
import dev.forcetower.cubicrectangle.view.details.helpers.UIAlphaFrame
import kotlinx.coroutines.CoroutineScope
import timber.log.Timber
import javax.inject.Inject

class DetailsFragment : BaseFragment(), DetailsContract.View {
    @Inject
    lateinit var factory: BaseViewModelFactory
    private lateinit var frame: UIAlphaFrame
    private lateinit var binding: FragmentDetailsBinding
    private val args: DetailsFragmentArgs by navArgs()
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
            binding.contract = this@DetailsFragment
            val initial = if (resources.getBoolean(R.bool.on_night_mode)) Color.BLACK else Color.WHITE
            frame = UIAlphaFrame(it.root, 500, initial)
        }.also {
            Timber.d("This is running")
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.doOnApplyWindowInsets { v, insets, padding ->
            Timber.d("YASSS ${insets.systemWindowInsetBottom}")
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
        view.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        presenter.loadMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
            onMovieUpdate(it)
        })
    }

    private fun onMovieUpdate(movie: Movie?) {
        movie ?: return
        binding.movie = movie
        prepareImageAndColors(movie)
    }

    private fun prepareImageAndColors(movie: Movie) {
        val url = movie.backdropPath ?: movie.posterPath ?: return
        val link = "https://image.tmdb.org/t/p/w780$url"
        Glide.with(this).load(link)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    binding.titleImageHorizontal.fadeIn()
                    if (resource is BitmapDrawable) {
                        val palette = Palette.from(resource.bitmap).generate()
                        var vibrant = palette.getVibrantColor(Color.WHITE)
                        if (vibrant == -1) vibrant = palette.getDominantColor(Color.WHITE)
                        frame.changeAlphaTo(vibrant)
                    } else {
                        frame.changeAlphaTo(Color.BLACK)
                    }
                    return false
                }
            })
            .into(binding.titleImageHorizontal)
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onLoadError(@StringRes resource: Int) {
        showSnack(getString(resource), Snackbar.LENGTH_LONG)
    }

    override fun onNavigateBack() {
        findNavController().popBackStack()
    }

    override fun shouldApplyBottomInsets() = false
    override fun shouldApplyTopInsets() = false

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
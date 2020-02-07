package dev.forcetower.cubicrectangle.view.details

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionSet.ORDERING_TOGETHER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import android.transition.ArcMotion
import android.transition.ChangeBounds
import android.transition.ChangeClipBounds
import android.transition.ChangeImageTransform
import android.transition.ChangeTransform
import android.transition.TransitionSet
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseFragment
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.bindings.ImageLoadListener
import dev.forcetower.cubicrectangle.core.extensions.fadeIn
import dev.forcetower.cubicrectangle.databinding.FragmentDetailsBinding
import dev.forcetower.cubicrectangle.model.aggregation.MovieAndGenres
import dev.forcetower.cubicrectangle.view.details.helpers.UIAlphaFrame
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.TimeUnit
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

    private var currentLink: String? = null

    private val imageLoad = object : ImageLoadListener {
        override fun onImageLoaded() {
            startPostponedEnterTransition()
        }

        override fun onImageLoadFailed() {
            startPostponedEnterTransition()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition(500, TimeUnit.MILLISECONDS)
        val transition = TransitionSet()
            .addTransition(ChangeBounds().apply {
                pathMotion = ArcMotion()
            })
            .addTransition(ChangeTransform())
            .addTransition(ChangeClipBounds())
            .addTransition(ChangeImageTransform())
            .setOrdering(ORDERING_TOGETHER)
            .setInterpolator(FastOutSlowInInterpolator())

        sharedElementEnterTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.attach(this)
        return FragmentDetailsBinding.inflate(inflater, container, false).also {
            binding = it
            binding.listener = imageLoad
            binding.contract = this@DetailsFragment
            val initial = if (resources.getBoolean(R.bool.on_night_mode)) Color.BLACK else Color.WHITE
            frame = UIAlphaFrame(it.root, 500, initial)
            it.includeAdditional.layoutExtraInfo.visibility = View.VISIBLE
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.loadMovieDetails(args.movieId).observe(viewLifecycleOwner, Observer {
            onMovieUpdate(it)
        })

        val set = AnimationSet(false)
        val translation = TranslateAnimation(0f, 0f, 800f, 0f).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = 350
        }
        val fade = AlphaAnimation(0f, 1f).apply {
            interpolator = LinearInterpolator()
            duration = 250
            startOffset = 100
        }
        set.addAnimation(translation)
        set.addAnimation(fade)
        binding.includeAdditional.layoutExtraInfo.startAnimation(set)
        set.start()
    }

    private fun onMovieUpdate(movie: MovieAndGenres?) {
        movie ?: return
        binding.aggregation = movie
        prepareImageAndColors(movie)
    }

    private fun prepareImageAndColors(aggregation: MovieAndGenres) {
        val movie = aggregation.movie
        val url = movie.backdropPath ?: movie.posterPath ?: return
        val link = "https://image.tmdb.org/t/p/w780$url"
        if (link == currentLink) return
        currentLink = link
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

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}
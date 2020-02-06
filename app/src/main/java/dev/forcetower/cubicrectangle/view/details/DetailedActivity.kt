package dev.forcetower.cubicrectangle.view.details

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.navigation.navArgs
import androidx.palette.graphics.Palette
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.base.BaseActivity
import dev.forcetower.cubicrectangle.core.base.BaseViewModelFactory
import dev.forcetower.cubicrectangle.core.bindings.ImageLoadListener
import dev.forcetower.cubicrectangle.core.extensions.getBitmap
import dev.forcetower.cubicrectangle.core.extensions.postponeEnterTransition
import dev.forcetower.cubicrectangle.core.ui.widget.ElasticDragDismissFrameAlternative
import dev.forcetower.cubicrectangle.core.ui.widget.ElasticDragDismissFrameLayout
import dev.forcetower.cubicrectangle.core.utils.AnimUtils.getFastOutSlowInInterpolator
import dev.forcetower.cubicrectangle.core.utils.ColorUtils
import dev.forcetower.cubicrectangle.core.utils.ViewUtils
import dev.forcetower.cubicrectangle.databinding.ActivityDetailsBinding
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DetailedActivity : BaseActivity(), DetailsContract.View {
    private lateinit var chromeFader: ElasticDragDismissFrameAlternative.SystemChromeFader
    @Inject
    lateinit var factory: BaseViewModelFactory
    private lateinit var binding: ActivityDetailsBinding
    private val viewModel by viewModels<DetailsViewModel> { factory }
    private val args: DetailedActivityArgs by navArgs()
    private val presenter: DetailsContract.Presenter
        get() = viewModel.presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition(500L)
        presenter.attach(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)

        presenter.loadMovieDetails(args.movieId).observe(this, Observer {
            binding.movie = it
        })

        val headLoadListener = object : ImageLoadListener {
            override fun onImageLoaded(resource: Drawable) {
                val bitmap = resource.getBitmap() ?: return

                Palette.from(bitmap)
                    .clearFilters()
                    .generate { palette -> applyFullImagePalette(palette) }

                val twentyFourDip = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    24f,
                    resources.displayMetrics
                ).toInt()
                Palette.from(bitmap)
                    .maximumColorCount(3)
                    .clearFilters()
                    .setRegion(0, 0, bitmap.width - 1, twentyFourDip)
                    .generate { palette -> applyTopPalette(bitmap, palette) }
                binding.image.background = null
                startPostponedEnterTransition()
            }
            override fun onImageLoadFailed() {
                binding.image.background = null
                startPostponedEnterTransition()
            }
        }

        binding.imageListener = headLoadListener

        chromeFader = object : ElasticDragDismissFrameAlternative.SystemChromeFader(this) {
            override fun onDragDismissed() {
                finishAfterTransition()
            }
        }

        binding.apply {
            bodyScroll.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                image.offset = -scrollY
            }
            back.setOnClickListener { finishAfterTransition() }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.draggableFrame.addListener(chromeFader)
    }

    override fun onPause() {
        binding.draggableFrame.removeListener(chromeFader)
        super.onPause()
    }

    override fun onBackPressed() {
        finishAfterTransition()
    }

    override fun onNavigateUp(): Boolean {
        finishAfterTransition()
        return true
    }

    override fun getLifecycleScope(): CoroutineScope {
        return viewModel.viewModelScope
    }

    override fun onLoadError(resource: Int) {
    }

    fun applyFullImagePalette(palette: Palette?) {
        // color the ripple on the image spacer (default is grey)
        binding.imageSpacer.background = ViewUtils.createRipple(
            palette, 0.25f, 0.5f,
            ContextCompat.getColor(this, R.color.mid_grey), true
        )
        // slightly more opaque ripple on the pinned image to compensate for the scrim
        binding.image.foreground = ViewUtils.createRipple(
            palette, 0.3f, 0.6f,
            ContextCompat.getColor(this, R.color.mid_grey), true
        )
    }

    fun applyTopPalette(bitmap: Bitmap, palette: Palette?) {
        val lightness = ColorUtils.isDark(palette)
        val isDark = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
            ColorUtils.isDark(bitmap, bitmap.width / 2, 0)
        } else {
            lightness == ColorUtils.IS_DARK
        }

        // color the status bar.
        var statusBarColor = window.statusBarColor
        ColorUtils.getMostPopulousSwatch(palette)?.let {
            statusBarColor = ColorUtils.scrimify(it.rgb, isDark, SCRIM_ADJUSTMENT)
            // set a light status bar
            if (!isDark) {
                ViewUtils.setLightStatusBar(binding.image)
            }
        }

        if (statusBarColor != window.statusBarColor) {
            binding.image.setScrimColor(statusBarColor)
            ValueAnimator.ofArgb(window.statusBarColor, statusBarColor).apply {
                addUpdateListener { animation ->
                    window.statusBarColor = animation.animatedValue as Int
                }
                duration = 1000L
                interpolator = getFastOutSlowInInterpolator(this@DetailedActivity)
            }.start()
        }
    }

    companion object {
        private const val SCRIM_ADJUSTMENT = 0.075f
    }
}
package dev.forcetower.cubicrectangle.core.ui.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import dev.forcetower.cubicrectangle.R
import dev.forcetower.cubicrectangle.core.extensions.isDarkTheme

/**
 * A transition that animates the RGB scale of an [ImageView]s `drawable` when in dark mode.
 */
class DarkenImage(context: Context, attrs: AttributeSet) : Transition(context, attrs) {

    private val isDarkTheme = context.isDarkTheme()
    private val initialRgbScale: Float
    private val finalRgbScale: Float

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DarkenImage)
        initialRgbScale = ta.getFloat(R.styleable.DarkenImage_initialRgbScale, 1.0f)
        finalRgbScale = ta.getFloat(R.styleable.DarkenImage_finalRgbScale, 1.0f)
        ta.recycle()
    }

    override fun captureStartValues(transitionValues: TransitionValues?) { }

    override fun captureEndValues(transitionValues: TransitionValues?) { }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (!isDarkTheme) return null
        if (initialRgbScale == finalRgbScale) return null
        val iv = endValues?.view as? ImageView ?: return null
        val drawable = iv.drawable ?: return null
        return ValueAnimator.ofFloat(initialRgbScale, finalRgbScale).apply {
            addUpdateListener { listener ->
                val cm = ColorMatrix()
                val rgbScale = listener.animatedValue as Float
                cm.setScale(rgbScale, rgbScale, rgbScale, 1.0f)
                drawable.colorFilter = ColorMatrixColorFilter(cm)
            }
        }
    }
}
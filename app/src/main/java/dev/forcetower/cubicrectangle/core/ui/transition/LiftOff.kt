package dev.forcetower.cubicrectangle.core.ui.transition

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import dev.forcetower.cubicrectangle.R

/**
 * A transition that animates the elevation of a View from a given value down to zero.
 *
 *
 * Useful for creating parent↔child navigation transitions
 * (https://www.google.com/design/spec/patterns/navigational-transitions.html#navigational-transitions-parent-to-child)
 * when combined with a [android.transition.ChangeBounds] on a shared element.
 */
class LiftOff(context: Context, attrs: AttributeSet?) : Transition(context, attrs) {
    private val initialElevation: Float
    private val finalElevation: Float

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.LiftOff)
        initialElevation = ta.getDimension(R.styleable.LiftOff_initialElevation, 0f)
        finalElevation = ta.getDimension(R.styleable.LiftOff_finalElevation, 0f)
        ta.recycle()
    }

    override fun getTransitionProperties(): Array<String> {
        return transitionProps
    }

    override fun captureStartValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ELEVATION] = initialElevation
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        transitionValues.values[PROPNAME_ELEVATION] = finalElevation
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues,
        endValues: TransitionValues
    ): Animator {
        return ObjectAnimator.ofFloat(
            endValues.view, View.TRANSLATION_Z,
            initialElevation, finalElevation
        )
    }

    companion object {
        private const val PROPNAME_ELEVATION = "cubic:liftoff:elevation"
        private val transitionProps = arrayOf(PROPNAME_ELEVATION)
    }
}
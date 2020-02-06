package dev.forcetower.cubicrectangle.core.ui.transition

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Rect
import android.transition.ChangeBounds
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.ViewGroup
import dev.forcetower.cubicrectangle.core.ui.widget.ParallaxScrimageView

/**
 * An extension to [ChangeBounds] designed to work with [ParallaxScrimageView]. This
 * will remove any parallax applied while also performing a `ChangeBounds` transition.
 */
class DeparallaxChangeBounds(
    context: Context?,
    attrs: AttributeSet?
) : ChangeBounds(context, attrs) {
    override fun captureEndValues(transitionValues: TransitionValues) {
        super.captureEndValues(transitionValues)

        if (transitionValues.view !is ParallaxScrimageView) return
        val psv: ParallaxScrimageView = transitionValues.view as ParallaxScrimageView

        if (psv.offset == 0) return
        val bounds = transitionValues.values[PROPNAME_BOUNDS] as Rect
        bounds.offset(0, psv.offset)
        transitionValues.values[PROPNAME_BOUNDS] = bounds
    }

    override fun createAnimator(
        sceneRoot: ViewGroup,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        val changeBounds = super.createAnimator(sceneRoot, startValues, endValues)
        if (startValues == null || endValues == null || endValues.view !is ParallaxScrimageView) return changeBounds

        val psv: ParallaxScrimageView = endValues.view as ParallaxScrimageView
        if (psv.offset == 0) return changeBounds
        val deparallax = ObjectAnimator.ofInt(psv, ParallaxScrimageView.OFFSET, 0)
        val transition = AnimatorSet()
        transition.playTogether(changeBounds, deparallax)
        return changeBounds
    }

    companion object {
        private const val PROPNAME_BOUNDS = "android:changeBounds:bounds"
    }
}

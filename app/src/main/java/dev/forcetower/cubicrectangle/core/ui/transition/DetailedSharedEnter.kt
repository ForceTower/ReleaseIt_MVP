package dev.forcetower.cubicrectangle.core.ui.transition

import android.content.Context
import android.graphics.Rect
import android.transition.ChangeBounds
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View

/**
 * Shared element transitions do not seem to like transitioning from a single view to two separate
 * views so we need to alter the ChangeBounds transition to compensate
 */
class DetailedSharedEnter(
    context: Context?,
    attrs: AttributeSet?
) : ChangeBounds(context, attrs) {
    override fun captureEndValues(transitionValues: TransitionValues) {
        super.captureEndValues(transitionValues)
        val width = (transitionValues.values[PROPNAME_PARENT] as View).width
        val bounds = transitionValues.values[PROPNAME_BOUNDS] as Rect
        bounds.right = width
        bounds.bottom = width * 14 / 9
        transitionValues.values[PROPNAME_BOUNDS] = bounds
    }

    companion object {
        private const val PROPNAME_BOUNDS = "android:changeBounds:bounds"
        private const val PROPNAME_PARENT = "android:changeBounds:parent"
    }
}
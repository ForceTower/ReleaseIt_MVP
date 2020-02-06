package dev.forcetower.cubicrectangle.core.ui.transition

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import dev.forcetower.cubicrectangle.core.utils.ViewUtils

/**
 * A transition which fades in/out the background [Drawable] of a View.
 */
class BackgroundFade : Visibility {
    constructor() : super()
    @Keep
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onAppear(
        sceneRoot: ViewGroup?,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (view == null || view.background == null) return null
        val background = view.background
        background.alpha = 0
        return ObjectAnimator.ofInt<Drawable>(background, ViewUtils.DRAWABLE_ALPHA, 0, 255)
    }

    override fun onDisappear(
        sceneRoot: ViewGroup,
        view: View?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        return if (view == null || view.background == null) null else ObjectAnimator.ofInt<Drawable>(
            view.background,
            ViewUtils.DRAWABLE_ALPHA,
            0
        )
    }
}
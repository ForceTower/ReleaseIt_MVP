package dev.forcetower.cubicrectangle.view.details.helpers

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import dev.forcetower.cubicrectangle.R

class UIAlphaFrame(
    private val root: View,
    private val duration: Long = 1_000,
    initial: Int = Color.WHITE
) {
    private val edges: View? = root.findViewById(R.id.image_overlay)
    private var current = initial

    fun changeAlphaTo(color: Int) {
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), current, color)
        colorAnimation.duration = duration

        colorAnimation.addUpdateListener { animator: ValueAnimator ->
            root.setBackgroundColor(animator.animatedValue as Int)
            edges?.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                current = color
            }
        })
        colorAnimation.start()
    }
}
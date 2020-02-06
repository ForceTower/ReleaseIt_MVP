package dev.forcetower.cubicrectangle.core.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.view.updatePadding
import androidx.core.view.updatePaddingRelative
import com.google.android.material.internal.ViewUtils.requestApplyInsetsWhenAttached
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerFragment
import dev.forcetower.cubicrectangle.core.extensions.doOnApplyWindowInsets
import dev.forcetower.cubicrectangle.core.extensions.requestApplyInsetsWhenAttached
import timber.log.Timber

abstract class BaseFragment : DaggerFragment() {
    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (shouldApplyBottomInsets()) {
            view.doOnApplyWindowInsets { v, insets, padding ->
                v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
            }
        }
        if (shouldApplyTopInsets()) {
            view.doOnApplyWindowInsets { v, insets, padding ->
                v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
            }
        }
    }

    fun showSnack(string: String, duration: Int = Snackbar.LENGTH_SHORT) {
        val activity = activity
        if (activity is BaseActivity) {
            activity.showSnack(string, duration)
        } else {
            Timber.i("Not part of UActivity")
        }
    }

    fun getSnack(string: String, duration: Int = Snackbar.LENGTH_SHORT): Snackbar? {
        val activity = activity
        return if (activity is BaseActivity) {
            activity.getSnackInstance(string, duration)
        } else {
            Timber.i("Not part of UActivity")
            null
        }
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        view?.requestApplyInsetsWhenAttached()
    }

    open fun shouldApplyBottomInsets() = true
    open fun shouldApplyTopInsets() = true
}
package dev.forcetower.cubicrectangle.core.bindings

import android.view.View
import androidx.databinding.BindingAdapter
import dev.forcetower.cubicrectangle.core.ui.outline.CircularOutlineProvider
import dev.forcetower.cubicrectangle.core.ui.outline.RoundedOutlineProvider
@BindingAdapter("goneIf")
fun goneIf(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, condition: Boolean) {
    view.visibility = if (condition) View.VISIBLE else View.GONE
}

@BindingAdapter("roundedViewRadius")
fun clipToCircle(view: View, radius: Float) {
    view.clipToOutline = true
    view.outlineProvider = RoundedOutlineProvider(radius)
}

@BindingAdapter("clipToCircle")
fun clipToCircle(view: View, clip: Boolean) {
    view.clipToOutline = clip
    view.outlineProvider = if (clip) CircularOutlineProvider else null
}
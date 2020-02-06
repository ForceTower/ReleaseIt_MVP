package dev.forcetower.cubicrectangle.core.utils

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.Property
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.palette.graphics.Palette
import dev.forcetower.cubicrectangle.core.utils.ColorUtils.modifyAlpha

object ViewUtils {
    val DRAWABLE_ALPHA: Property<Drawable, Int> =
        AnimUtils.createIntProperty(object : AnimUtils.IntProp<Drawable>("alpha") {
            override operator fun set(`object`: Drawable, value: Int) {
                `object`.alpha = value
            }

            override operator fun get(`object`: Drawable): Int {
                return `object`.alpha
            }
        })

    fun createRipple(
        palette: Palette?,
        @FloatRange(from = 0.0, to = 1.0) darkAlpha: Float,
        @FloatRange(from = 0.0, to = 1.0) lightAlpha: Float,
        @ColorInt fallbackColor: Int,
        bounded: Boolean
    ): RippleDrawable? {
        var rippleColor = fallbackColor
        if (palette != null) { // try the named swatches in preference order
            when {
                palette.vibrantSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.vibrantSwatch!!.rgb,
                        darkAlpha
                    )
                }
                palette.lightVibrantSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.lightVibrantSwatch!!.rgb,
                        lightAlpha
                    )
                }
                palette.darkVibrantSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.darkVibrantSwatch!!.rgb,
                        darkAlpha
                    )
                }
                palette.mutedSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.mutedSwatch!!.rgb,
                        darkAlpha
                    )
                }
                palette.lightMutedSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.lightMutedSwatch!!.rgb,
                        lightAlpha
                    )
                }
                palette.darkMutedSwatch != null -> {
                    rippleColor = modifyAlpha(
                        palette.darkMutedSwatch!!.rgb,
                        darkAlpha
                    )
                }
            }
        }
        return RippleDrawable(
            ColorStateList.valueOf(rippleColor), null,
            if (bounded) ColorDrawable(Color.WHITE) else null
        )
    }

    fun setLightStatusBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags: Int = view.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            view.systemUiVisibility = flags
        }
    }
}
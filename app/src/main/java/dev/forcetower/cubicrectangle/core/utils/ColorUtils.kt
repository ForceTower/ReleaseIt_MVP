package dev.forcetower.cubicrectangle.core.utils

import android.graphics.Bitmap
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.core.graphics.ColorUtils
import androidx.core.math.MathUtils
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Palette.Swatch

object ColorUtils {
    const val IS_LIGHT = 0
    const val IS_DARK = 1
    const val LIGHTNESS_UNKNOWN = 2

    @CheckResult
    @ColorInt
    @JvmStatic
    fun modifyAlpha(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        return color and 0x00ffffff or (alpha shl 24)
    }

    @CheckResult
    @ColorInt
    @JvmStatic
    fun modifyAlpha(
        @ColorInt color: Int,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float
    ): Int {
        return modifyAlpha(color, (255f * alpha).toInt())
    }

    fun isDark(@ColorInt color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }

    fun isDark(palette: Palette?): Int {
        val mostPopulous = getMostPopulousSwatch(palette) ?: return LIGHTNESS_UNKNOWN
        return if (isDark(mostPopulous.rgb)) IS_DARK else IS_LIGHT
    }

    fun isDark(
        bitmap: Bitmap,
        backupPixelX: Int,
        backupPixelY: Int
    ): Boolean { // first try palette with a small color quant size
        val palette = Palette.from(bitmap).maximumColorCount(3).generate()
        return if (palette.swatches.size > 0) {
            isDark(palette) == IS_DARK
        } else { // if palette failed, then check the color of the specified pixel
            isDark(bitmap.getPixel(backupPixelX, backupPixelY))
        }
    }

    fun getMostPopulousSwatch(palette: Palette?): Swatch? {
        var mostPopulous: Swatch? = null
        if (palette != null) {
            for (swatch in palette.swatches) {
                if (mostPopulous == null || swatch.population > mostPopulous.population) {
                    mostPopulous = swatch
                }
            }
        }
        return mostPopulous
    }

    @ColorInt
    fun scrimify(
        @ColorInt color: Int,
        isDark: Boolean,
        @FloatRange(from = 0.0, to = 1.0) multiplier: Float
    ): Int {
        var lightnessMultiplier = multiplier
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(color, hsl)
        if (!isDark) {
            lightnessMultiplier += 1f
        } else {
            lightnessMultiplier = 1f - lightnessMultiplier
        }
        hsl[2] = MathUtils.clamp(hsl[2] * lightnessMultiplier, 0f, 1f)
        return ColorUtils.HSLToColor(hsl)
    }
}
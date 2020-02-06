package dev.forcetower.cubicrectangle.core.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@BindingAdapter(value = [
    "imageUrl",
    "clipCircle",
    "listener",
    "crossFade",
    "overrideImageWidth",
    "overrideImageHeight"
], requireAll = false)
fun imageUri(
    imageView: ImageView,
    imageUrl: String?,
    clipCircle: Boolean? = false,
    listener: ImageLoadListener?,
    crossFade: Boolean? = false,
    overrideWidth: Int? = null,
    overrideHeight: Int? = null
) {
    if (imageUrl == null) return

    var request = Glide.with(imageView).load(imageUrl)

    if (clipCircle == true) request = request.circleCrop()
    if (crossFade == true) request = request.transition(DrawableTransitionOptions.withCrossFade())
    if (overrideWidth != null && overrideHeight != null) {
        request = request.override(overrideWidth, overrideHeight)
    }

    if (listener != null) {
        request = request.listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                listener.onImageLoaded(resource)
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                listener.onImageLoadFailed()
                return false
            }
        })
    }
    request.into(imageView)
}

@BindingAdapter(value = [
    "imageTmdbUrl",
    "clipCircle",
    "listener",
    "crossFade",
    "overrideImageWidth",
    "overrideImageHeight"
], requireAll = false)
fun imageTmdbUrl(
    imageView: ImageView,
    imageUrl: String?,
    clipCircle: Boolean? = false,
    listener: ImageLoadListener? = null,
    crossFade: Boolean? = false,
    overrideWidth: Int? = null,
    overrideHeight: Int? = null
) {
    val url = if (imageUrl == null) null else "https://image.tmdb.org/t/p/w780$imageUrl"
    imageUri(imageView, url, clipCircle, listener, crossFade, overrideWidth, overrideHeight)
}

interface ImageLoadListener {
    fun onImageLoaded(resource: Drawable)
    fun onImageLoadFailed()
}
package dev.forcetower.cubicrectangle.core.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@BindingAdapter(value = [
    "imageUrl",
    "clipCircle",
    "listener",
    "dontTransform"
], requireAll = false)
fun imageUri(
    imageView: ImageView,
    imageUrl: String?,
    clipCircle: Boolean? = false,
    listener: ImageLoadListener? = null,
    dontTransform: Boolean? = false
) {
    imageUrl ?: return
    var request = Glide.with(imageView).load(imageUrl)
    if (clipCircle == true) request = request.circleCrop()
    if (dontTransform == true) request = request.dontTransform()

    if (listener != null) {
        request = request.listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                listener.onImageLoaded()
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
    "dontTransform"
], requireAll = false)
fun imageTmdbUrl(
    imageView: ImageView,
    imageUrl: String?,
    clipCircle: Boolean?,
    listener: ImageLoadListener?,
    dontTransform: Boolean? = false
) {
    val url = if (imageUrl == null) null else "https://image.tmdb.org/t/p/w780$imageUrl"
    imageUri(imageView, url, clipCircle, listener, dontTransform)
}

interface ImageLoadListener {
    fun onImageLoaded()
    fun onImageLoadFailed()
}
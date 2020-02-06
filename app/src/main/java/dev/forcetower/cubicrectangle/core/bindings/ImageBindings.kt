package dev.forcetower.cubicrectangle.core.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

@BindingAdapter(value = ["imageUrl", "clipCircle", "listener"], requireAll = false)
fun imageUri(
    imageView: ImageView,
    imageUrl: String?,
    clipCircle: Boolean?,
    listener: ImageLoadListener?
) {
    imageUrl ?: return
    val circular = clipCircle ?: false
    var request = Glide.with(imageView).load(imageUrl)
    if (circular) request = request.circleCrop()

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

@BindingAdapter(value = ["imageTmdbUrl", "clipCircle", "listener"], requireAll = false)
fun imageTmdbUrl(imageView: ImageView, imageUrl: String?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    val url = if (imageUrl == null) null else "https://image.tmdb.org/t/p/w780$imageUrl"
    imageUri(imageView, url, clipCircle, listener)
}

interface ImageLoadListener {
    fun onImageLoaded()
    fun onImageLoadFailed()
}
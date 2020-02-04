package dev.forcetower.cubicrectangle.core.bindings

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

@BindingAdapter(value = ["imageUri", "placeholder", "clipCircle", "listener"], requireAll = false)
fun imageUri(imageView: ImageView, imageUri: Uri?, placeholder: Drawable?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    val circular = clipCircle ?: false
    var request = when (imageUri) {
        null -> {
            Glide.with(imageView).load(placeholder)
        }
        else -> {
            Glide.with(imageView)
                .load(imageUri)
                .apply(RequestOptions().placeholder(placeholder))
        }
    }
    request = if (circular) {
        request.circleCrop()
    } else {
        request
    }

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

@BindingAdapter(value = ["imageUrl", "placeholder", "clipCircle", "listener"], requireAll = false)
fun imageUrl(imageView: ImageView, imageUrl: String?, placeholder: Drawable?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    imageUri(imageView, imageUrl?.toUri(), placeholder, clipCircle, listener)
}

@BindingAdapter(value = ["imageTmdbUrl", "placeholder", "clipCircle", "listener"], requireAll = false)
fun imageTmdbUrl(imageView: ImageView, imageUrl: String?, placeholder: Drawable?, clipCircle: Boolean?, listener: ImageLoadListener?) {
    val url = if (imageUrl == null) null else "https://image.tmdb.org/t/p/w780$imageUrl"
    imageUri(imageView, url?.toUri(), placeholder, clipCircle, listener)
}

interface ImageLoadListener {
    fun onImageLoaded()
    fun onImageLoadFailed()
}
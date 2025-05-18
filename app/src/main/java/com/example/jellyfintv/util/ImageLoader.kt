package com.example.jellyfintv.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.jellyfintv.R
import timber.log.Timber

/**
 * A utility class for loading images using Glide with common configurations.
 */
object ImageLoader {

    // Default placeholder and error drawable IDs
    @DrawableRes
    private const val DEFAULT_PLACEHOLDER = R.drawable.ic_image_placeholder
    @DrawableRes
    private const val DEFAULT_ERROR = R.drawable.ic_broken_image

    /**
     * Loads an image into an ImageView with default configurations.
     *
     * @param context The context.
     * @param url The URL of the image to load.
     * @param imageView The ImageView to load the image into.
     * @param placeholderResId Optional placeholder drawable resource ID.
     * @param errorResId Optional error drawable resource ID.
     * @param skipMemoryCache Whether to skip memory cache.
     * @param skipDiskCache Whether to skip disk cache.
     * @param centerCrop Whether to apply center crop transformation.
     * @param roundedCornersRadius Optional corner radius in pixels for rounded corners.
     * @param callback Optional callback for image loading events.
     */
    @JvmOverloads
    @JvmStatic
    fun loadImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        @DrawableRes placeholderResId: Int = DEFAULT_PLACEHOLDER,
        @DrawableRes errorResId: Int = DEFAULT_ERROR,
        skipMemoryCache: Boolean = false,
        skipDiskCache: Boolean = false,
        centerCrop: Boolean = true,
        @Px roundedCornersRadius: Int = 0,
        callback: ImageLoadCallback? = null
    ) {
        if (url.isNullOrEmpty()) {
            imageView.setImageResource(errorResId)
            callback?.onLoadFailed(Exception("URL is null or empty"), errorResId)
            return
        }

        try {
            var requestBuilder: RequestBuilder<Drawable> = Glide.with(context)
                .load(url)
                .placeholder(placeholderResId)
                .error(errorResId)
                .skipMemoryCache(skipMemoryCache)
                .diskCacheStrategy(
                    if (skipDiskCache) DiskCacheStrategy.NONE
                    else DiskCacheStrategy.AUTOMATIC
                )

            // Apply transformations
            if (centerCrop) {
                requestBuilder = requestBuilder.transform(CenterCrop())
            }

            if (roundedCornersRadius > 0) {
                requestBuilder = requestBuilder.transform(RoundedCorners(roundedCornersRadius))
            }

            // Apply request options
            val requestOptions = RequestOptions()
                .placeholder(placeholderResId)
                .error(errorResId)

            requestBuilder.apply(requestOptions)

            // Add listener if callback is provided
            if (callback != null) {
                requestBuilder.listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback.onLoadFailed(e, errorResId)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback.onLoadSuccess(resource, dataSource)
                        return false
                    }
                })
            }

            // Load the image
            requestBuilder.into(imageView)
        } catch (e: Exception) {
            Timber.e(e, "Error loading image: $url")
            imageView.setImageResource(errorResId)
            callback?.onLoadFailed(e, errorResId)
        }
    }

    /**
     * Loads a circular image into an ImageView.
     *
     * @param context The context.
     * @param url The URL of the image to load.
     * @param imageView The ImageView to load the image into.
     * @param placeholderResId Optional placeholder drawable resource ID.
     * @param errorResId Optional error drawable resource ID.
     */
    @JvmStatic
    fun loadCircularImage(
        context: Context,
        url: String?,
        imageView: ImageView,
        @DrawableRes placeholderResId: Int = R.drawable.ic_person_placeholder,
        @DrawableRes errorResId: Int = R.drawable.ic_person_placeholder
    ) {
        if (url.isNullOrEmpty()) {
            imageView.setImageResource(errorResId)
            return
        }

        try {
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(placeholderResId)
                .error(errorResId)
                .into(imageView)
        } catch (e: Exception) {
            Timber.e(e, "Error loading circular image: $url")
            imageView.setImageResource(errorResId)
        }
    }

    /**
     * Clears the image from an ImageView and cancels any pending loads.
     *
     * @param imageView The ImageView to clear.
     */
    @JvmStatic
    fun clear(imageView: ImageView) {
        try {
            Glide.with(imageView.context).clear(imageView)
        } catch (e: Exception) {
            Timber.e(e, "Error clearing image view")
        }
    }

    /**
     * Clears the memory cache on the main thread.
     *
     * @param context The context.
     */
    @JvmStatic
    fun clearMemoryCache(context: Context) {
        try {
            Glide.get(context).clearMemory()
        } catch (e: Exception) {
            Timber.e(e, "Error clearing memory cache")
        }
    }

    /**
     * Clears the disk cache on a background thread.
     *
     * @param context The context.
     */
    @JvmStatic
    fun clearDiskCache(context: Context) {
        try {
            Glide.get(context).apply {
                // Clear disk cache on a background thread
                clearDiskCache()
            }
        } catch (e: Exception) {
            Timber.e(e, "Error clearing disk cache")
        }
    }

    /**
     * Callback interface for image loading events.
     */
    interface ImageLoadCallback {
        /**
         * Called when the image is successfully loaded.
         *
         * @param resource The loaded drawable.
         * @param dataSource The data source the image was loaded from.
         */
        fun onLoadSuccess(resource: Drawable?, dataSource: DataSource?)

        /**
         * Called when the image fails to load.
         *
         * @param e The exception that caused the failure, or null if not available.
         * @param errorResId The error drawable resource ID that was set.
         */
        fun onLoadFailed(e: Exception?, @DrawableRes errorResId: Int)
    }
}

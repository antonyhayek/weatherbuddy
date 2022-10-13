package com.antonyhayek.weatherbuddy.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl


object ImageUtils {

   /* fun downloadImage(
        context: Context,
        filePath: String,
        imageView: ImageView,
        errorImageResource: Int
    ) {
        Glide.with(context).asBitmap().load(filePath)
            .error(errorImageResource).into(imageView)
    }*/

    fun downloadImage(
        context: Context,
        filePath: String,
        imageView: ImageView,
        errorImageResource: Int,
    ) {

        var imageUrl = "http://openweathermap.org/img/wn//$filePath@2x.png"

        if (filePath.isNotEmpty()) {
            Glide.with(context).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .load(GlideUrl(imageUrl)
                ).error(errorImageResource).into(imageView)
        }
    }

}
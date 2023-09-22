package com.example.psychology.base_app.utils.glide

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.example.psychology.base_app.utils.base.apputils.AppManager
import org.jetbrains.annotations.NotNull

/**
 * Author      : silveryTitan
 * Email       : silverytitan94@gmail.com
 * Date        : on 2021-08-28 09:01.
 * Description :
 */
object GlideUtils {
    fun setImg(@NotNull url: String): RequestBuilder<Drawable> {
        val context = AppManager.instance.getContext() ?: AppManager.instance.getAppContext()
        return Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    fun setGif(@NotNull url: String): RequestBuilder<GifDrawable> {
        val context = AppManager.instance.getContext() ?: AppManager.instance.getAppContext()
        return Glide.with(context).asGif().load(url)
    }

    fun setBitmap(@NotNull url: String): RequestBuilder<Bitmap> {
        val context = AppManager.instance.getContext() ?: AppManager.instance.getAppContext()
        return Glide.with(context).asBitmap().load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
    }



}
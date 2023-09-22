package com.example.psychology.base_app.utils.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Author      : wyw
 * Date        : on 2021-11-09 16:41.
 * Description :
 */
object ImageBindingAdapter {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun bindImageUrl(view: ImageView, imageUrl: String?) {
        val options =
            RequestOptions()
                .centerCrop()
                .dontAnimate()

        Glide.with(view)
            .load(imageUrl ?: "")
            .apply(options)
            .into(view)


    }

}
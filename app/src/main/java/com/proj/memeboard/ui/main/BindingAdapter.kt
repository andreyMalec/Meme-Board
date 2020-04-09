package com.proj.memeboard.ui.main

import android.widget.ImageView
import com.proj.memeboard.R
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun imageUrl(imageView: ImageView, urlToImage: String) {
        Glide.with(imageView).load(urlToImage).into(imageView)
    }
}
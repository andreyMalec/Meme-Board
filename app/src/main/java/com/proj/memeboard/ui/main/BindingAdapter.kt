package com.proj.memeboard.ui.main

import android.widget.ImageView
import android.widget.TextView
import com.proj.memeboard.R
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.proj.memeboard.util.DateFormatter

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun imageUrl(imageView: ImageView, urlToImage: String) {
        Glide.with(imageView).load(urlToImage).placeholder(R.drawable.image_placeholder).into(imageView)
    }

    @BindingAdapter("formatDate")
    @JvmStatic
    fun formatDate(textView: TextView, date: Long) {
        textView.text = DateFormatter(textView.context).formatDaysAgo(date)
    }
}
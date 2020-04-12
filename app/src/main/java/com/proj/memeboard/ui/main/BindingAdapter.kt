package com.proj.memeboard.ui.main

import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.proj.memeboard.util.DateFormatter

object BindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun imageUrl(imageView: ImageView, urlToImage: String) {
        Glide.with(imageView).load(urlToImage).into(imageView)
    }

    @BindingAdapter("htmlText")
    @JvmStatic
    fun htmlText(textView: TextView, htmlText: String?) {
        if (!htmlText.isNullOrBlank())
            textView.text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    @BindingAdapter("formatDate")
    @JvmStatic
    fun formatDate(textView: TextView, date: Long) {
        textView.text = DateFormatter(textView.context).formatDaysAgo(date)
    }
}
package com.proj.memeboard.util

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.proj.memeboard.R
import com.proj.memeboard.domain.Meme
import java.io.File

class MemeSharer(private val context: Context) {
    fun send(meme: Meme) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND

            if (meme.photoUrl?.contains(context.packageName) == true) {
                val uri = FileProvider.getUriForFile(context, context.packageName, File(meme.photoUrl))
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_TEXT, meme.title + "\n" + (meme.description ?: ""))
                type = "image/jpeg"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else
                putExtra(
                    Intent.EXTRA_TEXT, meme.title + "\n" +
                            (meme.description ?: "") + "\n" +
                            meme.photoUrl
                )

            type = "text/plain"
        }, context.getString(R.string.share_meme))
        context.startActivity(share)
    }
}
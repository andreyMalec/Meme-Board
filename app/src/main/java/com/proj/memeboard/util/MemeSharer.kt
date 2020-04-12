package com.proj.memeboard.util

import android.content.Context
import android.content.Intent
import com.proj.memeboard.R
import com.proj.memeboard.localDb.MemeData

class MemeSharer(private val context: Context) {
    fun send(meme: MemeData) {
        val share = Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
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
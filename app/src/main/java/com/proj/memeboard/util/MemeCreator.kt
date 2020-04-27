package com.proj.memeboard.util

import android.content.Context
import android.graphics.Bitmap
import com.proj.memeboard.domain.Meme
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class MemeCreator(private val context: Context, private val authorId: Long) {
    fun create(title: String, description: String?, image: Bitmap): Meme {
        val time = Calendar.getInstance().time.time
        val imageFile = createImageFile("meme$time.jpg")
        saveImage(image, imageFile)

        return Meme(
            id = time,
            title = title,
            description = description,
            isFavorite = true,
            createdDate = time / 1000,
            photoUrl = imageFile.absolutePath,
            author = authorId
        )
    }

    private fun createImageFile(path: String): File {
        return File(context.cacheDir, path).apply {
            createNewFile()
        }
    }

    private fun saveImage(image: Bitmap, imageFile: File) {
        val bos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        val bitmapData = bos.toByteArray()

        FileOutputStream(imageFile).apply {
            write(bitmapData)
            flush()
        }
    }
}
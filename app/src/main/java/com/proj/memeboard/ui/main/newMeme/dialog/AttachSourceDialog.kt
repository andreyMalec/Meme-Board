package com.proj.memeboard.ui.main.newMeme.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.proj.memeboard.R

class AttachSourceDialog(private val target: ListDialogListener) : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_attach, container, false)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        v.findViewById<ListView>(R.id.sourceList).setOnItemClickListener { _, _, position, _ ->
            val result = when (position) {
                0 -> DialogResult.GALLERY
                else -> DialogResult.CAMERA
            }
            target.onDialogFinish(result)

            dialog?.dismiss()
        }

        return v
    }

    interface ListDialogListener {
        fun onDialogFinish(result: DialogResult)
    }

    enum class DialogResult {
        GALLERY,
        CAMERA
    }
}
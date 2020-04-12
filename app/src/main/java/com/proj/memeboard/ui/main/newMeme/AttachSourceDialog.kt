package com.proj.memeboard.ui.main.newMeme

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.DialogFragment
import com.proj.memeboard.R

class AttachSourceDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_dialog_attach, container, false)

        v.findViewById<ListView>(R.id.sourceList).setOnItemClickListener {  _, _, position, _ ->
            (targetFragment as ListDialogListener).onDialogFinish(position)

            dialog?.dismiss()
        }

        return v
    }

    interface ListDialogListener {
        fun onDialogFinish(position: Int)
    }
}
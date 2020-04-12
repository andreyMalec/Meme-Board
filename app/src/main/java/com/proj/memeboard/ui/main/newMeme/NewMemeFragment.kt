package com.proj.memeboard.ui.main.newMeme

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.BuildConfig
import com.proj.memeboard.R
import com.proj.memeboard.localDb.MemeData
import kotlinx.android.synthetic.main.fragment_new_meme.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class NewMemeFragment : Fragment(), AttachSourceDialog.ListDialogListener {
    private lateinit var createMeme: MenuItem
    private lateinit var viewModel: NewMemeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_meme, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_meme_menu, menu)
        createMeme = menu.findItem(R.id.createNewMeme)
        createMeme.setOnMenuItemClickListener {
            val time = Calendar.getInstance().time.time

            val f = File(requireContext().cacheDir, "meme$time.jpg")
            f.createNewFile()

            val bos = ByteArrayOutputStream()
            viewModel.memeImage.value?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(f)
            fos.write(bitmapData)
            fos.flush()

            viewModel.addMeme(
                MemeData(
                    time,
                    titleInput.text.toString(),
                    descriptionInput.text.toString(),
                    true,
                    time,
                    f.absolutePath,
                    "userName"
                )
            )

            showMemeCreated()

            viewModel.memeImage.value = null
            viewModel.title.value = null
            titleInput.text = null
            descriptionInput.text = null

            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        viewModel = ViewModelProvider(this).get(NewMemeViewModel::class.java)

        addImageButton.setOnClickListener {
            val dialog = AttachSourceDialog()
            dialog.setTargetFragment(this, 9)
            dialog.show(parentFragmentManager, "SelectAttachSource")
        }

        clearImageButton.setOnClickListener {
            viewModel.memeImage.value = null
        }

        initViewModelListeners()
        initInputListeners()
    }

    private fun showMemeCreated() {
        val snackbar = Snackbar.make(main, getString(R.string.new_meme_created), Snackbar.LENGTH_LONG)
        snackbar.anchorView = activity?.findViewById(R.id.nav_view)
        snackbar.show()
    }

    private fun initViewModelListeners() {
        viewModel.memeImage.observe(viewLifecycleOwner, Observer { image ->
            if (image != null) {
                Glide.with(this).load(image).into(imageView)
                clearImageButton.visibility = View.VISIBLE
            }
            else {
                Glide.with(this).clear(imageView)
                clearImageButton.visibility = View.GONE
            }
            viewModel.checkCanCreate()
        })

        viewModel.canCreate.observe(viewLifecycleOwner, Observer {
            if (this::createMeme.isInitialized)
                createMeme.isEnabled = it
        })
    }

    private fun initInputListeners() {
        titleInput.doOnTextChanged { text, _, _, _ ->
            viewModel.title.value = text.toString()
            viewModel.checkCanCreate()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val newFile = File(requireContext().getExternalFilesDir(null), "myNewMeme.jpg")
            val k = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, newFile)

            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, k)
            startActivityForResult(takePhotoIntent, 11)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fixBitmap: Bitmap? = when {
            resultCode == Activity.RESULT_OK && data != null && requestCode == 10 -> {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
            }
            resultCode == Activity.RESULT_OK && data != null && requestCode == 11 -> {
                val newFile = File(requireContext().getExternalFilesDir(null), "myNewMeme.jpg")

                BitmapFactory.decodeFile(newFile.path)
            }
            else -> null
        }

        viewModel.memeImage.value = fixBitmap
    }

    override fun onDialogFinish(position: Int) {
        when(position) {
            0 -> startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 10)
            1 -> {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 11)
                } else {
                    onRequestPermissionsResult(11, arrayOf(), intArrayOf(PackageManager.PERMISSION_GRANTED))
                }
            }
        }
    }
}

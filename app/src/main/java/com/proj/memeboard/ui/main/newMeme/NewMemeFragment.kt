package com.proj.memeboard.ui.main.newMeme

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.BuildConfig
import com.proj.memeboard.R
import com.proj.memeboard.di.Injectable
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.CAMERA
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.GALLERY
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.TEMP_MEME_PATH
import com.proj.memeboard.ui.main.newMeme.dialog.AttachSourceDialog
import com.proj.memeboard.ui.main.newMeme.dialog.AttachSourceDialog.DialogResult
import kotlinx.android.synthetic.main.fragment_new_meme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewMemeFragment : Fragment(), AttachSourceDialog.ListDialogListener, Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: NewMemeViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var createMeme: MenuItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_meme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolBar()
        initListeners()
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorLightBackground
            )
        )
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_new_meme)
        setHasOptionsMenu(true)
    }

    private fun initListeners() {
        initViewModelListeners()
        initInputListeners()
        initButtonListeners()
    }

    private fun initViewModelListeners() {
        viewModel.memeImage.observe(viewLifecycleOwner, Observer { image ->
            if (image != null) {
                Glide.with(this).load(image).into(imageView)
                clearImageButton.visibility = View.VISIBLE
            } else {
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
            viewModel.title.value = text?.toString()
            viewModel.checkCanCreate()
        }

        descriptionInput.doAfterTextChanged {
            viewModel.description.value = it?.toString()
        }
    }

    private fun initButtonListeners() {
        addImageButton.setOnClickListener {
            showImageDialog()
        }

        clearImageButton.setOnClickListener {
            viewModel.memeImage.value = null
        }
    }

    private fun showImageDialog() {
        val dialog = AttachSourceDialog()
        dialog.setTargetFragment(this, 9)
        dialog.show(parentFragmentManager, "SelectAttachSource")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startCameraActivity()
    }

    private fun startCameraActivity() {
        val newMemeFile = File(requireContext().getExternalFilesDir(null), TEMP_MEME_PATH)
        val extraFile = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID, newMemeFile)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, extraFile)
        startActivityForResult(takePhotoIntent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        getImageFromResult(requestCode, resultCode, data)
    }

    private fun getImageFromResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val fixBitmap: Bitmap? = when {
            resultCode == Activity.RESULT_OK && data != null && requestCode == GALLERY -> {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data.data)
            }
            resultCode == Activity.RESULT_OK && requestCode == CAMERA -> {
                val newFile = File(requireContext().getExternalFilesDir(null), TEMP_MEME_PATH)

                BitmapFactory.decodeFile(newFile.path)
            }
            else -> null
        }

        viewModel.memeImage.value = fixBitmap
    }

    override fun onDialogFinish(result: DialogResult) {
        when (result) {
            DialogResult.GALLERY -> {
                startActivityForResult(
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
                    GALLERY
                )
            }
            DialogResult.CAMERA -> {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    startCameraActivity()
                else
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.new_meme_menu, menu)

        createMeme = menu.findItem(R.id.createNewMeme)
        createMeme.isEnabled = viewModel.canCreate.value ?: false
        createMeme.setOnMenuItemClickListener {
            viewModel.createMeme()
            showMemeCreated()
            clearInput()
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showMemeCreated() {
        val snackbar = Snackbar.make(main, getString(R.string.new_meme_created), Snackbar.LENGTH_LONG)
        snackbar.anchorView = activity?.findViewById(R.id.bottom_nav_view)
        snackbar.setBackgroundTint(ContextCompat.getColor(this.requireContext(), R.color.colorAccent))
        snackbar.show()
    }

    private fun clearInput() {
        titleInput.text = null
        descriptionInput.text = null

        titleInput.clearFocus()
        descriptionInput.clearFocus()
    }
}
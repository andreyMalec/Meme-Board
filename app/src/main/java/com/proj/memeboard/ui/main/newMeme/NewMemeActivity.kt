package com.proj.memeboard.ui.main.newMeme

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.BuildConfig
import com.proj.memeboard.R
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.CAMERA
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.GALLERY
import com.proj.memeboard.ui.main.newMeme.NewMemeViewModel.Companion.TEMP_MEME_PATH
import com.proj.memeboard.ui.main.newMeme.dialog.AttachSourceDialog
import com.proj.memeboard.ui.main.newMeme.dialog.AttachSourceDialog.DialogResult
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_new_meme.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NewMemeActivity : AppCompatActivity(), AttachSourceDialog.ListDialogListener, HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: NewMemeViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var createMeme: MenuItem

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_meme)

        initToolBar()
        initListeners()
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorLightBackground
            )
        )
        toolbar.title = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initListeners() {
        initViewModelListeners()
        initInputListeners()
        initButtonListeners()
    }

    private fun initViewModelListeners() {
        viewModel.image.observe(this, Observer { image ->
            if (image != null) {
                imageView.setImageBitmap(image)
                clearImageButton.visibility = View.VISIBLE
            } else {
                imageView.setImageResource(0)
                clearImageButton.visibility = View.GONE
            }
            viewModel.checkCanCreate()
        })

        viewModel.canCreate.observe(this, Observer {
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
            viewModel.clearImage()
        }
    }

    private fun showImageDialog() {
        val dialog = AttachSourceDialog(this)
        dialog.show(supportFragmentManager, "SelectAttachSource")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            startCameraActivity()
    }

    private fun startCameraActivity() {
        val newMemeFile = File(cacheDir, TEMP_MEME_PATH)
        val extraFile = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, newMemeFile)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, extraFile)
        startActivityForResult(takePhotoIntent, CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        viewModel.getImageFromResult(requestCode, resultCode, data)
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
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    startCameraActivity()
                else
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA)
                    else
                        startCameraActivity()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.createNewMeme -> {
                viewModel.createMeme()
                showMemeCreated()
                clearInput()
                hideKeyboard()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_meme_menu, menu)
        createMeme = menu?.findItem(R.id.createNewMeme) ?: return false
        createMeme.isEnabled = viewModel.canCreate.value ?: false

        return true
    }

    private fun showMemeCreated() {
        Snackbar.make(root, getString(R.string.new_meme_created), Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorAccent))
            .show()
    }

    private fun clearInput() {
        titleInput.text = null
        descriptionInput.text = null

        titleInput.clearFocus()
        descriptionInput.clearFocus()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(titleInput.windowToken, 0)
    }
}
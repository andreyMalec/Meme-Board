package com.proj.memeboard.ui.main.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.proj.memeboard.R
import com.proj.memeboard.databinding.ActivityMemeDetailBinding
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.util.MemeSharer
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_meme_detail.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MemeDetailActivity : AppCompatActivity(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: MemeDetailViewModel by viewModels {
        viewModelFactory
    }

    private lateinit var binding: ActivityMemeDetailBinding
    private lateinit var deleteButton: MenuItem

    companion object {
        fun getExtraIntent(context: Context, meme: Meme): Intent {
            return Intent(context, MemeDetailActivity::class.java).apply {
                putExtra("id", meme.id)
                putExtra("title", meme.title)
                putExtra("description", meme.description)
                putExtra("isFavorite", meme.isFavorite)
                putExtra("createdDate", meme.createdDate)
                putExtra("photoUrl", meme.photoUrl)
                putExtra("author", meme.author)
            }
        }
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meme_detail_menu, menu)
        menu?.let {
            deleteButton = it.findItem(R.id.deleteButton)
            viewModel.setDeleteVisibility()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
            R.id.shareButton -> viewModel.currentMeme.value?.let {
                MemeSharer(this).send(it)
            }
            R.id.deleteButton -> {
                viewModel.deleteMeme()
                supportFinishAfterTransition()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.currentMeme.value = getExtraMeme()
        binding = setContentView(this, R.layout.activity_meme_detail)
        binding.meme = viewModel.currentMeme.value

        initViewModelListener()
        initToolBar()
        initFavoriteListener()
    }

    private fun initViewModelListener() {
        viewModel.isDeleteVisible.observe(this, Observer { visibility ->
            if (this::deleteButton.isInitialized) {
                deleteButton.isVisible = visibility
            }
        })
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorLightBackground
            )
        )
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = viewModel.currentMeme.value?.title
    }

    private fun initFavoriteListener() {
        binding.favoriteLayout.setOnClickListener {
            val favBtn = it.findViewById<CheckBox>(R.id.favoriteButton)
            favBtn.isChecked = !favBtn.isChecked

            viewModel.toggleFavorite()
        }
    }

    private fun getExtraMeme(): Meme {
        val intent = intent
        return Meme(
            intent.getLongExtra("id", 0),
            intent.getStringExtra("title"),
            intent.getStringExtra("description"),
            intent.getBooleanExtra("isFavorite", false),
            intent.getLongExtra("createdDate", 0),
            intent.getStringExtra("photoUrl"),
            intent.getLongExtra("author", 0)
        )
    }
}

package com.proj.memeboard.ui.main.home.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.ViewModelProvider
import com.proj.memeboard.R
import com.proj.memeboard.databinding.ActivityMemeDetailBinding
import com.proj.memeboard.localDb.MemeData
import com.proj.memeboard.ui.main.home.BaseMemeViewModel
import com.proj.memeboard.util.MemeSharer
import kotlinx.android.synthetic.main.activity_main.*

class MemeDetailActivity : AppCompatActivity() {
    private lateinit var meme: MemeData
    private lateinit var binding: ActivityMemeDetailBinding
    private lateinit var viewModel: BaseMemeViewModel

    companion object {
        fun getExtraIntent(context: Context, meme: MemeData): Intent {
            return Intent(context, MemeDetailActivity::class.java).apply {
                putExtra("id", meme.id)
                putExtra("title", meme.title)
                putExtra("description", meme.description)
                putExtra("isFavorite", meme.isFavorite)
                putExtra("createdDate", meme.createdDate)
                putExtra("photoUrl", meme.photoUrl)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.meme_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
            R.id.shareButton -> MemeSharer(this).send(meme)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(BaseMemeViewModel::class.java)

        meme = getExtraMeme()
        binding = setContentView(this, R.layout.activity_meme_detail)
        binding.meme = meme

        initToolBar()
        initFavoriteListener()
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
        supportActionBar?.title = meme.title
    }

    private fun initFavoriteListener() {
        binding.favoriteLayout.setOnClickListener {
            val favBtn = it.findViewById<CheckBox>(R.id.favoriteButton)
            favBtn.isChecked = !favBtn.isChecked

            val updatedMeme = MemeData(
                meme.id,
                meme.title,
                meme.description,
                favBtn.isChecked,
                meme.createdDate,
                meme.photoUrl
            )
            viewModel.updateMeme(updatedMeme)
        }
    }

    private fun getExtraMeme(): MemeData {
        val intent = intent
        return MemeData(
            intent.getLongExtra("id", 0),
            intent.getStringExtra("title"),
            intent.getStringExtra("description"),
            intent.getBooleanExtra("isFavorite", false),
            intent.getLongExtra("createdDate", 0),
            intent.getStringExtra("photoUrl")
        )
    }
}

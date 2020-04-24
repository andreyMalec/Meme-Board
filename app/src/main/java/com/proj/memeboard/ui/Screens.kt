package com.proj.memeboard.ui

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.ui.login.LoginActivity
import com.proj.memeboard.ui.main.MainActivity
import com.proj.memeboard.ui.main.detail.MemeDetailActivity
import com.proj.memeboard.ui.main.home.MemeFragment
import com.proj.memeboard.ui.main.newMeme.NewMemeActivity
import com.proj.memeboard.ui.main.user.UserFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.android.support.SupportAppScreen

@ExperimentalCoroutinesApi
object Screens {
    object LoginScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, LoginActivity::class.java)
        }
    }

    object MainScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, MainActivity::class.java)
        }
    }

    object HomeScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return MemeFragment()
        }
    }

    object NewMemeScreen : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return Intent(context, NewMemeActivity::class.java)
        }
    }

    object ProfileScreen : SupportAppScreen() {
        override fun getFragment(): Fragment? {
            return UserFragment()
        }
    }

    class DetailScreen(
        private val meme: Meme,
        private val cardTransitionOptions: Pair<View, String>
    ) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return createExtraIntent(context, meme)
        }

        fun getOptions() = cardTransitionOptions

        companion object {
            fun createExtraIntent(context: Context, meme: Meme): Intent {
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

            fun parseExtraIntent(intent: Intent): Meme =
                Meme(
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
}
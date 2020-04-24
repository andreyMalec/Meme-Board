package com.proj.memeboard.ui

import android.content.Context
import android.content.Intent
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

    class DetailScreen(private val meme: Meme) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return MemeDetailActivity.getExtraIntent(context, meme)
        }
    }
}
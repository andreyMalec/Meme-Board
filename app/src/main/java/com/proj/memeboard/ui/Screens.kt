package com.proj.memeboard.ui

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.proj.memeboard.domain.Meme
import com.proj.memeboard.ui.login.LoginActivity
import com.proj.memeboard.ui.main.MainActivity
import com.proj.memeboard.ui.main.detail.MemeDetailActivity
import com.proj.memeboard.ui.main.home.MemeFragment
import com.proj.memeboard.ui.main.newMeme.NewMemeFragment
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
        private var fragment: Fragment? = null

        override fun getFragment(): Fragment? {
            if (fragment == null)
                fragment = MemeFragment()

            return fragment
        }

        fun clearFragment() {
            fragment = null
        }
    }

    object NewMemeScreen : SupportAppScreen() {
        private var fragment: Fragment? = null

        override fun getFragment(): Fragment? {
            if (fragment == null)
                fragment = NewMemeFragment()

            return fragment
        }

        fun clearFragment() {
            fragment = null
        }
    }

    object ProfileScreen : SupportAppScreen() {
        private var fragment: Fragment? = null

        override fun getFragment(): Fragment? {
            if (fragment == null)
                fragment = UserFragment()

            return fragment
        }

        fun clearFragment() {
            fragment = null
        }
    }

    class DetailScreen(private val meme: Meme) : SupportAppScreen() {
        override fun getActivityIntent(context: Context): Intent? {
            return MemeDetailActivity.getExtraIntent(context, meme)
        }
    }
}
package com.proj.memeboard.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.proj.memeboard.R
import com.proj.memeboard.ui.login.LoginActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SplashActivity : AppCompatActivity() {
    private val splashScreenDuration = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startLoginActivity()
    }

    private fun startLoginActivity() {
        Handler().postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, splashScreenDuration)
    }
}

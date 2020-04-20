package com.proj.memeboard.ui.login

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.R
import com.proj.memeboard.ui.main.MainActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), HasActivityInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initListeners()
        setProgressBarColor()
    }

    private fun initListeners() {
        initViewModelListeners()
        setInputListeners()
        setLoginButtonListener()
    }

    private fun initViewModelListeners() {
        viewModel.isUserAuthorized.observe(this, Observer { authorized ->
            if (authorized)
                startMemeActivity()
        })

        viewModel.isLoading.observe(this, Observer { loading ->
            if (loading) showProgress()
            else hideProgress()
        })

        viewModel.isLoadError.observe(this, Observer { error ->
            if (error) showLoginError()
        })

        viewModel.isLoginInputError.observe(this, Observer { error ->
            loginLayout.error =
                if (error) getString(R.string.blank_input_error)
                else null
        })

        viewModel.passInputError.observe(this, Observer { error ->
            when (error) {
                0 -> passLayout.error = getString(R.string.blank_input_error)
                1 -> passLayout.helperText = getString(R.string.password_helper)

                else -> {
                    passLayout.error = null
                    passLayout.helperText = null
                }
            }
        })
    }

    private fun startMemeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showLoginError() {
        Snackbar.make(root, getString(R.string.login_fail), Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
            .show()
    }

    private fun setInputListeners() {
        setLoginInputListeners()
        setPassInputListeners()
    }

    private fun setLoginInputListeners() {
        loginEditText.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                viewModel.checkLoginInput(s?.toString())

                super.afterTextChanged(s)
            }
        })
    }

    private fun setPassInputListeners() {
        passEditText.doAfterTextChanged {
            viewModel.checkPassInput(it?.toString())
        }
    }

    private fun setLoginButtonListener() {
        loginButton.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val login = loginEditText.text.toString()
        val pass = passEditText.text.toString()

        viewModel.authorizeUser(login, pass)
    }

    private fun setProgressBarColor() {
        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )
    }

    private fun hideProgress() {
        loginButton.text = getString(R.string.sign_in)
        loginButton.isClickable = true
        progressBar.visibility = View.GONE

    }

    private fun showProgress() {
        loginButton.text = ""
        loginButton.isClickable = false
        progressBar.visibility = View.VISIBLE
    }
}
package com.proj.memeboard.ui.login

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.proj.memeboard.ui.main.MainActivity
import com.proj.memeboard.R
import com.proj.memeboard.model.request.LoginRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    private val passwordSize = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        initListeners()
        setProgressBarColor()
    }

    private fun initListeners() {
        initViewModelListeners()
        setInputListeners()
        setLoginButtonListener()
    }

    private fun initViewModelListeners() {
        viewModel.userAuthorized.observe(this, Observer { authorized ->
            if (authorized)
                startMemeActivity()
        })

        viewModel.isLoading.observe(this, Observer { loading ->
            if (loading) showProgress()
            else hideProgress()
        })

        viewModel.loginError.observe(this, Observer { error ->
            if (error) showLoginError()
        })

        viewModel.userResult.observe(this, Observer {
            //иначе не будет обрабатываться изменение LoginRequest
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
                loginLayout.error =
                    if (loginEditText.text.isNullOrBlank())
                        getString(R.string.blank_input_error)
                    else null

                super.afterTextChanged(s)
            }
        })

        loginEditText.setOnFocusChangeListener { _, hasFocus ->
            loginLayout.error = if (hasFocus) loginLayout.error else null
        }
    }

    private fun setPassInputListeners() {
        passEditText.doAfterTextChanged {
            passLayout.error =
                if (passEditText.text.isNullOrBlank())
                    getString(R.string.blank_input_error)
                else null
            passLayout.helperText = null
        }

        passEditText.setOnFocusChangeListener { _, hasFocus ->
            passLayout.helperText =
                if (hasFocus && passEditText.text.isNullOrBlank())
                    getString(R.string.password_helper)
                else null
        }
    }

    private fun setLoginButtonListener() {
        loginButton.setOnClickListener {
            if (hasInputErrors()) showInputErrors()
            else login()
        }
    }

    private fun hasInputErrors(): Boolean =
        loginEditText.text.isNullOrBlank() || passEditText.text.isNullOrBlank() || passEditText.text?.count() != passwordSize

    private fun showInputErrors() {
        val blankInputError = getString(R.string.blank_input_error)
        loginLayout.error = if (loginEditText.text.isNullOrBlank()) blankInputError else null

        passLayout.error = if (passEditText.text?.count() != passwordSize) {
            if (passEditText.text.isNullOrBlank())
                blankInputError
            else
                getString(R.string.password_helper)
        } else null
    }

    private fun login() {
        val login = loginEditText.text.toString()
        val pass = passEditText.text.toString()

        viewModel.userInputData.value = LoginRequest(login, pass)
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
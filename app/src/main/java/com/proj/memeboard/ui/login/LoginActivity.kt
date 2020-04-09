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
import com.proj.memeboard.ui.MainActivity
import com.proj.memeboard.R
import com.proj.memeboard.model.LoginRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        if (viewModel.containsUserData()) {
            startMemeActivity()
            return
        }

        viewModel.user.observe(this, Observer { result ->
            hideProgress()

            when {
                result.isSuccess -> {
                    viewModel.saveUserData(result.getOrNull())

                    startMemeActivity()
                }
                result.isFailure -> { showLoginError() }
            }
        })

        setInputListeners()

        progressBar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        loginButton.setOnClickListener {
            if (hasInputErrors()) return@setOnClickListener

            showProgress()
            login()
        }
    }

    private fun startMemeActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun hideProgress() {
        loginButton.text = getString(R.string.sign_in)
        loginButton.isClickable = true
        progressBar.visibility = View.GONE
    }

    private fun showLoginError() {
        Snackbar.make(root, getString(R.string.login_fail), Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(this, R.color.colorError)).show()
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
                        getString(R.string.password_helper)
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
            passLayout.helperText =
                if (passEditText.text.isNullOrBlank())
                    getString(R.string.password_helper)
                else null
        }

        passEditText.setOnFocusChangeListener { _, hasFocus ->
            passLayout.helperText =
                if (hasFocus && passEditText.text.isNullOrBlank())
                    getString(R.string.password_helper)
                else null
        }
    }

    private fun hasInputErrors(): Boolean {
        val blankInputError = getString(R.string.blank_input_error)
        loginLayout.error = if (loginEditText.text.isNullOrBlank()) blankInputError else null
        passLayout.error = if (passEditText.text.isNullOrBlank()) blankInputError else null

        return loginLayout.error != null || passLayout.error != null
    }

    private fun showProgress() {
        loginButton.text = ""
        loginButton.isClickable = false
        progressBar.visibility = View.VISIBLE
    }

    private fun login() {
        val login = loginEditText.text.toString()
        val pass = passEditText.text.toString()

        viewModel.userInputData.value = LoginRequest(login, pass)
    }
}
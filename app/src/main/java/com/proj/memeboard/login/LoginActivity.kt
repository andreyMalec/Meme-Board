package com.proj.memeboard.login

import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import com.proj.memeboard.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        loginEditText.setOnFocusChangeListener { _, hasFocus ->
            loginLayout.error = if (hasFocus) loginLayout.error else null
        }

        passEditText.doAfterTextChanged {
            passLayout.helperText = if (passEditText.text.isNullOrBlank()) getString(R.string.password_helper) else null
        }

        passEditText.setOnFocusChangeListener { _, hasFocus ->
            passLayout.helperText = if (hasFocus && passEditText.text.isNullOrBlank()) getString(R.string.password_helper) else null
        }

        progressBar.indeterminateDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN)

        loginButton.setOnClickListener {
            if (!hasInputErrors()) return@setOnClickListener

            showProgress()
        }
    }

    private fun showProgress() {
        loginButton.text = ""
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        loginButton.text = getString(R.string.sign_in)
        progressBar.visibility = View.GONE
    }

    private fun hasInputErrors(): Boolean {
        val blankInputError = getString(R.string.blank_input_error)
        loginLayout.error = if (loginEditText.text.isNullOrBlank()) blankInputError else null
        passLayout.error = if (passEditText.text.isNullOrBlank()) blankInputError else null

        return loginLayout.error == null && passLayout.error == null
    }
}

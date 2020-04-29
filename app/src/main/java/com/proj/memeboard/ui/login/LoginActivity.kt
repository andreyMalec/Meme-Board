package com.proj.memeboard.ui.login

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
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
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navHolder: NavigatorHolder

    val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    private val navigator = SupportAppNavigator(this, -1)

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

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

    private fun showLoginError() {
        Snackbar.make(root, getString(R.string.login_fail), Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            progressBar.indeterminateDrawable.colorFilter = BlendModeColorFilter(R.color.colorPrimary, BlendMode.SRC_IN)
        else
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
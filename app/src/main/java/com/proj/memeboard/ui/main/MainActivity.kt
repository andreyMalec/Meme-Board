package com.proj.memeboard.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.proj.memeboard.R
import com.proj.memeboard.ui.main.navigation.BottomNavigator
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navHolder: NavigatorHolder

    val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    private val navigator = BottomNavigator(
        this,
        supportFragmentManager,
        R.id.nav_host_fragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
        initViewModelListeners()
        initBottomNavigation()
        viewModel.init()
    }

    private fun initViewModelListeners() {
        viewModel.showPressAgain.observe(this, Observer { show ->
            if (show)
                Toast.makeText(this, getString(R.string.pressAgainExit), Toast.LENGTH_SHORT).show()
        })
    }

    private fun initToolBar() {
        toolbar.setBackgroundColor(
            ContextCompat.getColor(
                this,
                R.color.colorLightBackground
            )
        )
        setSupportActionBar(toolbar)
    }

    private fun initBottomNavigation() {
        bottom_nav_view.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    viewModel.onHomeClick(); true
                }
                R.id.navigation_new_meme -> {
                    viewModel.onNewClick(); false
                }
                R.id.navigation_user -> {
                    viewModel.onProfileClick(); true
                }
                else -> false
            }
        }
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}
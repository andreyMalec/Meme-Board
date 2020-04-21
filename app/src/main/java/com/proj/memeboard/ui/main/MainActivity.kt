package com.proj.memeboard.ui.main

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.proj.memeboard.R
import com.proj.memeboard.ui.Screens
import com.proj.memeboard.ui.main.navigation.BottomNavigator
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.NavigatorHolder
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navHolder: NavigatorHolder

    val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    private val navigator = BottomNavigator(
        this,
        supportFragmentManager,
        R.id.nav_host_fragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolBar()
        initBottomNavigation()
        viewModel.init()
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
                    viewModel.onNewClick(); true
                }
                R.id.navigation_user -> {
                    viewModel.onProfileClick(); true
                }
                else -> false
            }
        }
    }

    override fun onDestroy() {
        Screens.HomeScreen.clearFragment()
        Screens.NewMemeScreen.clearFragment()
        Screens.ProfileScreen.clearFragment()

        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

    private var pressAgain = true
    private val pressAgainAwait = 2000L
    override fun onBackPressed() {
        if (pressAgain) {
            pressAgain = false
            Toast.makeText(this, getString(R.string.pressAgainExit), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ pressAgain = true }, pressAgainAwait)
            return
        }

        super.onBackPressed()
    }
}

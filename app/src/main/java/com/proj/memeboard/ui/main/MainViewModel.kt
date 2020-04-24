package com.proj.memeboard.ui.main

import androidx.lifecycle.ViewModel
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(private val router: Router) : ViewModel() {
    fun init() {
        onHomeClick()
    }

    fun onHomeClick() {
        router.replaceScreen(Screens.HomeScreen)
    }

    fun onNewClick() {
        router.navigateTo(Screens.NewMemeScreen)
    }

    fun onProfileClick() {
        router.replaceScreen(Screens.ProfileScreen)
    }
}
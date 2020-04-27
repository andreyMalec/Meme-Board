package com.proj.memeboard.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MainViewModel @Inject constructor(private val router: Router) : ViewModel() {

    private var pressAgain = true
    private val pressAgainAwait = 2000L

    private val _showPressAgain = MutableLiveData(false)
    val showPressAgain: LiveData<Boolean>
        get() = _showPressAgain

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

    fun onBackPressed() {
        if (pressAgain) {
            pressAgain = false
            viewModelScope.launch {
                delay(pressAgainAwait)
                pressAgain = true
            }
            _showPressAgain.value = true
            return
        }
        router.exit()
    }
}
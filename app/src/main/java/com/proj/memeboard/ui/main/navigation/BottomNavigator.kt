package com.proj.memeboard.ui.main.navigation

import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.proj.memeboard.ui.Screens
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

@ExperimentalCoroutinesApi
class BottomNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    private val screens = mutableListOf<String>()
    private var currentScreen: String = ""

    override fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen
        val screenKey = screen.screenKey

        if (currentScreen.isNotEmpty() && screenKey == currentScreen) return
        else currentScreen = screenKey

        fragmentManager.beginTransaction().apply {
            hideAll()
            show(screen)
            commit()
        }
    }

    override fun createStartActivityOptions(command: Command, activityIntent: Intent): Bundle? {
        return if (command is Forward && command.screen is Screens.DetailScreen) {
            val detailScreen = command.screen as Screens.DetailScreen
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, detailScreen.getOptions())
            options.toBundle()
        } else super.createStartActivityOptions(command, activityIntent)
    }

    private fun FragmentTransaction.show(screen: SupportAppScreen) {
        val fragment = createFragment(screen) ?: return

        if (!screens.contains(screen.screenKey)) {
            screens.add(screen.screenKey)
            add(containerId, fragment, screen.screenKey)
        }

        fragmentManager.findFragmentByTag(screen.screenKey)?.let {
            show(it)
        }
    }

    private fun FragmentTransaction.hideAll() {
        screens.forEach { key ->
            fragmentManager.findFragmentByTag(key)?.let {
                hide(it)
            }
        }
    }
}
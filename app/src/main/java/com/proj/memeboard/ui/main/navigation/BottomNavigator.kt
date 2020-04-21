package com.proj.memeboard.ui.main.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Replace

class BottomNavigator(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    containerId: Int
) : SupportAppNavigator(activity, fragmentManager, containerId) {

    private val fragments = mutableListOf<Fragment>()
    private lateinit var lastFragment: Fragment

    override fun fragmentReplace(command: Replace) {
        val screen = command.screen as SupportAppScreen

        val fragment = createFragment(screen) ?: return
        if (::lastFragment.isInitialized && fragment == lastFragment) return
        else lastFragment = fragment

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragments.forEach {
            fragmentTransaction.hide(it)
        }

        if (fragments.contains(fragment)) {
            fragmentTransaction.apply {
                show(fragment)
                commit()
            }
        } else {
            fragments.add(fragment)
            fragmentTransaction.apply {
                add(containerId, fragment)
                show(fragment)
                commit()
            }
        }
    }
}
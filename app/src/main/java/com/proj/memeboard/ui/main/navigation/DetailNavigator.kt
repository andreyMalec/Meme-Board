package com.proj.memeboard.ui.main.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.cicerone.commands.Forward

class DetailNavigator(activity: FragmentActivity, private val options: Bundle?) : SupportAppNavigator(activity, -1) {
    override fun activityForward(command: Forward) {
        val screen = command.screen as SupportAppScreen
        val activityIntent = screen.getActivityIntent(activity)

        if (activityIntent != null) {
            activity.startActivity(activityIntent, options)
        } else {
            fragmentForward(command)
        }
    }
}
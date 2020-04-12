package com.proj.memeboard.util

import android.content.Context
import com.proj.memeboard.R
import java.util.*

class DateFormatter(private val context: Context) {
    private val millisecondsInDay: Long = 1000 * 60 * 60 * 24

    fun formatDaysAgo(date: Long): String {
        val milliData = date * 1000
        val currentDate = Calendar.getInstance().time.time
        val diff = currentDate - milliData
        val diffDays = diff / millisecondsInDay

        return when {
            diffDays <= 1 -> context.getString(R.string.date_today)
            diffDays > 1 && diff < 2 -> context.getString(R.string.date_yesterday)
            else -> context.getString(R.string.date_n_days_ago, diffDays)
        }
    }
}
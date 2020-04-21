package com.proj.memeboard.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.proj.memeboard.R

class FirebaseService : FirebaseMessagingService() {
    private val notificationID = 321

    override fun onMessageReceived(message: RemoteMessage) {

        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                message.notification?.channelId,
                message.notification?.channelId,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = message.notification?.channelId
            channel.enableVibration(true)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, message.notification?.channelId ?: "").apply {
            setSmallIcon(R.drawable.icon)
            setContentTitle(message.notification?.title)
            setContentText(message.notification?.body)
            setWhen(System.currentTimeMillis())
            setVibrate(longArrayOf(500))
        }.build()

        notificationManager.notify(notificationID, builder)

        super.onMessageReceived(message)
    }
}
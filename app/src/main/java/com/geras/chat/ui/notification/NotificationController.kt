package com.geras.chat.ui.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.geras.chat.R

object NotificationController {

    fun createNotification(context: Context, notificationManager: NotificationManager, message: CharSequence) {

        val channel: NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel(
                "my_id",
                "Notification custom channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "this channel provides the space to notifications"
            notificationManager.createNotificationChannel(channel)
        }

        val notification =
            NotificationCompat.Builder(context, "my_id")
                .setAutoCancel(true)
                .setColor(Color.YELLOW)
                .setSmallIcon(R.drawable.ic_baseline_chat_bubble_24)
                .setContentTitle("New message")
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_SOUND)
                .build()

        notificationManager.notify(1, notification)
    }
}

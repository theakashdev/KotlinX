package com.akashdev.kotlinx

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap

const val NOTIFICATION_ID = "notification_id"
const val NOTIFICATION_TYPE = "notification_type"

fun Context.showPushNotification(
    notificationId: Int,
    title: String,
    body: String,
    pendingIntent: PendingIntent,
    buttonName: String? = null,
    smallIcon: Int? = null,
    smallIconColor: Int? = null,
) {
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(this, "channel_id").apply {
        setContentTitle(title)
        setContentText(body)
        smallIcon?.let { setSmallIcon(it) }
        smallIconColor?.let { setColor(it) }
        setAutoCancel(true)
        setContentIntent(pendingIntent)
    }

    //add action if not null
    buttonName?.let { notification.addAction(0, it, pendingIntent) }

    smallIcon?.let {
        notification.setLargeIcon(
            ContextCompat.getDrawable(this, it)?.toBitmap(100, 100)
        )
    }

    // Since android Oreo notification channel is needed.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel =
            NotificationChannel("channel_id", "channelName", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)
    }
    //show the notification
    notificationManager.notify(notificationId, notification.build())
}


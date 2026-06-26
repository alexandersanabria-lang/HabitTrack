package com.habittrack

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class HabitFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "HabitTrack"
        val body = remoteMessage.notification?.body ?: "Tienes un nuevo mensaje"

        showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí se podría enviar el token al servidor si fuera necesario
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "fcm_channel"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Notificaciones Push",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificaciones push de HabitTrack"
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1002, notification)
    }
}
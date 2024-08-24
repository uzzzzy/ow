package id.my.avzy.oneway.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.my.avzy.oneway.R
import id.my.avzy.oneway.service.ForegroundService.Companion.CHANNEL_ID
import id.my.avzy.oneway.ui.MainActivity

class PushNotificationService: FirebaseMessagingService() {
    companion object {
        const val CHANNEL_ID = "PushNotificationServiceChannel"
        const val NOTIFICATION_ID = 2
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        // Send token to your server
        Log.d("PushNotificationService", "onNewToken: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("PushNotificationService", "onMessageReceived: ${message.notification?.body}")

        val notification = createNotification(message.notification?.title.toString(), message.notification?.body.toString())
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        Log.d("PushNotificationService", "createNotificationChannel")
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Oneway",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(title: String, body: String): Notification {
        Log.d("PushNotificationService", "createNotification: $title")
        val fullScreenIntent = Intent(this, MainActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(1)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()
    }
}
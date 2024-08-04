package id.my.avzy.oneway

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


data class Message(val content: String, val sender: String)

class ForegroundService: Service() {
    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        const val NOTIFICATION_ID = 1
    }

    private var lastBatteryPct: Int = 0 // store last battery

    private val batteryReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent !== null) {
                val level = intent.getIntExtra("level", -1)
                val scale = intent.getIntExtra("scale", -1)
                if (level != -1 && scale != -1 ) {

                    if (level == lastBatteryPct) {
                        Log.d("ForegroundService", "No Change")
                        return
                    }
                    lastBatteryPct = level

                    // Update notification message here
                    updateNotification(level)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val notification = createNotification(0)
        startForeground(NOTIFICATION_ID, notification)

        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val sleepTime: Long = 1 // seconds
        val runnable = Runnable{
//            while (true) {
//                Log.d("ForegroundService", "HelloWorld")
                Thread.sleep(sleepTime * 1000)
//            }
        }

        val thread = Thread(runnable)

        thread.start()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Oneway Background",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager = getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(battery:Int): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service Running")
            .setContentText("Service is Running $battery")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private fun updateNotification(battery: Int) {
        val notification = createNotification(battery)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
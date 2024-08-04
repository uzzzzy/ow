package id.my.avzy.oneway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import id.my.avzy.oneway.service.ForegroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, ForegroundService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}

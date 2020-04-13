package to.freebots.todobutler.common.startup

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            startService(context)
        }
    }


    fun startService(context: Context?) {
        // todo reminder and location
    }

    companion object {
        fun listenOnBootEnabled(context: Context, enabled: Boolean) {
            val receiver = ComponentName(context, BootReceiver::class.java)

            context.packageManager.setComponentEnabledSetting(
                receiver,
                if (enabled) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                },
                PackageManager.DONT_KILL_APP
            )
        }


    }
}
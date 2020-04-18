package to.freebots.todobutler.models.logic

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.logic.android.NotificationService


class ReminderNotificationService(val application: Application) {

    private val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun requestCode(e: FlatTaskDTO): Int {
        return e.id!!.toInt()
    }

    fun createAlarm(task: FlatTaskDTO) {
        val alarmIntent = intent(task)

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            task.reminder!!.date.time,
            alarmIntent
        )
    }

    fun cancelAlarm(task: FlatTaskDTO) {
        val alarmIntent = intent(task)

        alarmManager.cancel(alarmIntent)

        NotificationService.cancelNotification(application, NotificationService.NotificationType.REMINDER, task)
    }

    private fun intent(task: FlatTaskDTO): PendingIntent {
        return Intent(application, NotificationReceiver::class.java).apply {
            putExtra(
                NotificationService.INTENT_TYPE_EXTRA,
                NotificationService.NotificationType.REMINDER.name
            )
            putExtra(
                NotificationService.INTENT_TASK_ID_EXTRA,
                task.id
            )
        }.let { intent ->
            PendingIntent.getBroadcast(application, requestCode(task), intent, 0)
        }
    }

    class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context != null && intent != null) {
                NotificationService.enqueueWork(context, intent)
            }
        }
    }
}
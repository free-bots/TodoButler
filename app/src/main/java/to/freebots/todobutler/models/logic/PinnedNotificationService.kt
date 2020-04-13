package to.freebots.todobutler.models.logic

import android.app.Application
import android.content.Intent
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.logic.ReminderNotificationService.NotificationReceiver
import to.freebots.todobutler.models.logic.android.NotificationService

class PinnedNotificationService(val application: Application) {

    fun pin(e: FlatTaskDTO) {
        NotificationService.enqueueWork(application, intent(e))
    }

    fun unPin(e: FlatTaskDTO) {
        NotificationService.cancelNotification(application, NotificationService.NotificationType.PINNED, e)
    }

    private fun intent(e: FlatTaskDTO):Intent {
        return Intent(application, NotificationReceiver::class.java).apply {
            putExtra(
                NotificationService.INTENT_TYPE_EXTRA,
                NotificationService.NotificationType.PINNED.name
            )
            putExtra(
                NotificationService.INTENT_TASK_ID_EXTRA,
                e.id
            )
        }
    }
}
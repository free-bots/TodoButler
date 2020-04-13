package to.freebots.todobutler.models.logic

import android.app.Application
import io.reactivex.Observable
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Reminder

class ReminderService(application: Application) : BaseLogicService<Reminder>(application) {

    val notificationService by lazy {
        ReminderNotificationService(application)
    }

    override fun findAll(): MutableList<Reminder> {
        return reminderDAO.findAll()
    }

    override fun findById(id: Long): Reminder {
        return reminderDAO.findById(id)
    }

    override fun create(e: Reminder): Reminder {
        return reminderDAO.createReminder(e)
    }

    override fun update(e: Reminder): Reminder {
        return reminderDAO.updateReminder(e)
    }

    override fun delete(e: Reminder): Reminder {
        reminderDAO.delete(e)
        return e
    }

    fun findAllDue(): MutableList<Reminder> {
        return mutableListOf()
    }

    fun createRx(e: Reminder): Observable<Reminder> {
        return Observable.fromCallable {
            create(e)
        }
    }

    fun updateRx(e: Reminder): Observable<Reminder> {
        return Observable.fromCallable {
            update(e)
        }
    }

    fun deleteRx(e: Reminder): Observable<Reminder> {
        return Observable.fromCallable {
            delete(e)
        }
    }

    fun createCopy(e: Reminder?): Reminder? {
        return if (e == null) {
            null
        } else {
            reminderDAO.createCopy(e)
        }
    }
}

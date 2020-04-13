package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Reminder

class ReminderService(application: Application) : BaseLogicService<Reminder>(application) {

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

    fun createCopy(e: Reminder?): Reminder? {
        return if (e == null) {
            null
        } else {
            reminderDAO.createCopy(e)
        }
    }
}

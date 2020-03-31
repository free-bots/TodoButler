package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Reminder

class ReminderService(application: Application) : BaseLogicService<Reminder>(application) {

    override fun findAll(): MutableList<Reminder> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): Reminder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(e: Reminder): Reminder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: Reminder): Reminder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: Reminder): Reminder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findAllDue(): MutableList<Reminder> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

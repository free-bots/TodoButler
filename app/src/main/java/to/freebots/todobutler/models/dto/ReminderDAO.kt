package to.freebots.todobutler.models.dto

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Reminder

@Dao
abstract class ReminderDAO : BaseDAO<Reminder> {

    @Query("SELECT * FROM Reminder")
    abstract fun findAll(): MutableList<Reminder>

    @Query("SELECT * FROM Reminder WHERE id=:id")
    abstract fun findById(id: Long): Reminder

    @Query("SELECT * FROM Reminder WHERE rowid=:rowId")
    abstract fun findByRowId(rowId: Long): Reminder

    @Transaction
    open fun createReminder(e: Reminder): Reminder {
        return findByRowId(create(e))
    }

    @Transaction
    open fun updateReminder(e: Reminder): Reminder {
        update(e)
        return findById(e.id!!)
    }
}
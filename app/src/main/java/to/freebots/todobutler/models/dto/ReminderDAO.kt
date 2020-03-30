package to.freebots.todobutler.models.dto

import androidx.room.Dao
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Reminder

@Dao
abstract class ReminderDAO: BaseDAO<Reminder> {
}
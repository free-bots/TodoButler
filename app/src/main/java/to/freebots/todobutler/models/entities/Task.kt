package to.freebots.todobutler.models.entities

import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Task(
    var labelId: Long,
    var parentTaskId: Long?,
    var name: String,
    var description: String,
    var isCompleted: Boolean,
    id: Long? = 0,
    createdAt: Date? = null,
    updatedAt: Date? = null
) :
    BaseEntity(id, createdAt, updatedAt) {
}
// TODO: location, reminder

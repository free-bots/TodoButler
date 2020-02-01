package to.freebots.todobutler.models.entities

import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Attachment(
    var taskId: Long,
    var name: String,
    var path: String,
    id: Long = 0,
    createdAt: Date? = null,
    updatedAt: Date? = null
) :
    BaseEntity(id, createdAt, updatedAt) {
}
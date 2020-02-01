package to.freebots.todobutler.models.entities

import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Label(
    var name: String,
    id: Long = 0,
    createdAt: Date? = null,
    updatedAt: Date? = null
) : BaseEntity(id, createdAt, updatedAt) {
}
// TODO: add icon

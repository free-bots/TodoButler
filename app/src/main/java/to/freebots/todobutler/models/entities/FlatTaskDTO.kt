package to.freebots.todobutler.models.entities

import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

class FlatTaskDTO(
    var label: Label,
    var parentTaskId: Long?,
    var name: String,
    var description: String,
    var isCompleted: Boolean,
    var subTasks: MutableList<FlatTaskDTO>,
    var attachments: MutableList<Attachment>,
    id: Long = 0,
    createdAt: Date? = null,
    updatedAt: Date? = null
) :
    BaseEntity(id, createdAt, updatedAt) {
}
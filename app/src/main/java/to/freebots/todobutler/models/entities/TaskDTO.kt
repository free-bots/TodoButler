package to.freebots.todobutler.models.entities

import androidx.room.Embedded
import androidx.room.Relation


data class TaskDTO(
    @Embedded
    var task: Task,

    @Relation(
        parentColumn = "labelId",
        entityColumn = "id"
    )
    var label: Label,

    @Relation(
        parentColumn = "locationId",
        entityColumn = "id"
    )
    var location: Location?,

    @Relation(
        parentColumn = "reminderId",
        entityColumn = "id"
    )
    var reminder: Reminder?,

    @Relation(
        parentColumn = "id",
        entityColumn = "parentTaskId"
    )
    var subTasks: MutableList<Task>,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    var attachments: MutableList<Attachment>
)

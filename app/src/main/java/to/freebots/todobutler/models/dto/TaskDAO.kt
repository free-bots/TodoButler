package to.freebots.todobutler.models.dto

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

@Dao
abstract class TaskDAO : BaseDAO<Task> {

    @Query("SELECT * from Task")
    abstract fun findAll(): MutableList<Task>

    @Query("SELECT * FROM Task WHERE id=:id")
    abstract fun findById(id: Long): Task

    @Transaction
    @Query("SELECT * from Task WHERE parentTaskId ISNULL")
    abstract fun findAllDTO(): MutableList<TaskDTO>

    @Transaction
    @Query("SELECT * from Task WHERE id=:id")
    abstract fun findDTOById(id: Long): TaskDTO


    fun findAllFlatTaskDTO(): MutableList<FlatTaskDTO> {
        return findAllDTO().map { taskDTO ->
            flatDTO(taskDTO)
        }.toMutableList()
    }

    /**
     * converts the database representation to a object relation
     * @param taskDTO database representation
     */
    private fun flatDTO(
        taskDTO: TaskDTO
    ): FlatTaskDTO {
        val subTasks: MutableList<FlatTaskDTO> =
            taskDTO.subTasks.map { task: Task -> flatDTO(findDTOById(task.id)) }.toMutableList()

        return FlatTaskDTO(
            label = taskDTO.label,
            name = taskDTO.task.name,
            description = taskDTO.task.description,
            isCompleted = taskDTO.task.isCompleted,
            createdAt = taskDTO.task.createdAt,
            updatedAt = taskDTO.task.updatedAt,
            id = taskDTO.task.id,
            attachments = taskDTO.attachments,
            parentTaskId = taskDTO.task.parentTaskId,
            subTasks = subTasks
        )
    }
}

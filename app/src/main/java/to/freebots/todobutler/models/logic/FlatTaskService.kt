package to.freebots.todobutler.models.logic

import android.app.Application
import io.reactivex.Flowable
import io.reactivex.functions.Function
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

class FlatTaskService(application: Application) : BaseLogicService<FlatTaskDTO>(application) {

    // todo add rx observer

    val flatTaskObservable: Flowable<MutableList<FlatTaskDTO>> =
        database.taskDAO().findAllDTOFlowable().map(taskToFlatTaskDTO())


    private val _flatTasks = listOf<FlatTaskDTO>()

    override fun findAll(): MutableList<FlatTaskDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: Long): FlatTaskDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(e: FlatTaskDTO): FlatTaskDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: FlatTaskDTO): FlatTaskDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO): FlatTaskDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    /**
     * converts the database representation to a object relation
     * @param taskDTO database representation
     */
    private fun flatDTO(
        taskDTO: TaskDTO
    ): FlatTaskDTO {
        val subTasks: MutableList<FlatTaskDTO> =
            taskDTO.subTasks.map { task: Task -> flatDTO(taskDAO.findDTOById(task.id)) }
                .toMutableList()

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

    private fun taskToFlatTaskDTO() = Function<MutableList<TaskDTO>, MutableList<FlatTaskDTO>> {
        it.map { taskDTO ->
            flatDTO(
                taskDTO
            )
        }.toMutableList()
    }

}
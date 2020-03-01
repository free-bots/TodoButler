package to.freebots.todobutler.models.logic

import android.app.Application
import io.reactivex.Flowable
import io.reactivex.functions.Function
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

class FlatTaskService(
    application: Application,
    private val taskService: TaskService,
    private val attachmetService: AttachmetService
) :
    BaseLogicService<FlatTaskDTO>(application) {

    // todo add rx observer
    // todo add try catch and link to errorHandler

    val findAllFlatTasks: Flowable<MutableList<FlatTaskDTO>> =
        database.taskDAO().findAllDTOFlowable()
            .doOnError { t: Throwable? -> t?.let { errorChannel.onNext(it) } }
            .map(taskToFlatTaskDTO())


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
        // transform to the tasks
        val tasks: MutableList<FlatTaskDTO> = mutableListOf()
        flattenToTasks(e, tasks)

        val attachmentIds: List<Long> = tasks.flatMap { flatTaskDTO -> flatTaskDTO.attachments }.map { attachment -> attachment.id }.toList()
        attachmetService.deleteAllByIds(attachmentIds)

        taskService.deleteAllByIds(tasks.map { flatTaskDTO -> flatTaskDTO.id }.toMutableList())
        return e
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

    fun createFromCopy(flatTaskDTO: FlatTaskDTO): Long {
        return saveNewTasks(flatTaskDTO, flatTaskDTO.parentTaskId)
    }

    private fun saveNewTasks(e: FlatTaskDTO, id: Long?): Long {
//        val subTasks = e.subTasks
//        e.parentTaskId = id
//        val labelIndex = labelService.create(e.label).id
//        e.label.id = labelIndex
//        val rowIndex = create(flatTaskDTO_ToTaks(e)).id
//        subTasks.forEach {
//            saveNewTasks(it, rowIndex)
//        }
//        return rowIndex
        return 0
    }


    private fun flatTaskDTO_ToTaks(e: FlatTaskDTO): Task {
        return Task(
            e.label.id,
            e.parentTaskId,
            e.name,
            e.description,
            e.isCompleted,
            e.id,
            e.createdAt,
            e.updatedAt
        )
    }


    /**
     * makes a copy of the current task and adds it as a child to the parent task
     */
    fun copyIntoParent(e: FlatTaskDTO) {
        //todo overwrite ids
        // add to parent
        e.parentTaskId?.let {
            val parent = findFlatTaskDTOById(it)
            val copy =
                (copy(e, FlatTaskDTO.javaClass) as FlatTaskDTO).apply { parentTaskId = parent.id }
            saveCopyCascade(copy)
        } ?: run {
            // create a new task as parent
            val copy = copy(e, FlatTaskDTO.javaClass) as FlatTaskDTO
            saveCopyCascade(copy)
        }
    }

    private fun tasksToDelete(e: FlatTaskDTO): MutableList<Long> {
        val ids: MutableList<Long> = mutableListOf()
        tasksToDelete(e, ids)
        ids.reverse()
        return ids
    }

    private fun tasksToDelete(e: FlatTaskDTO, ids: MutableList<Long>) {
        ids.add(e.id)

        val subTasks = e.subTasks

        if (subTasks.isEmpty()) {
            return
        }

        subTasks.map { flatTaskDTO -> tasksToDelete(flatTaskDTO, ids) }
    }

    fun findFlatTaskDTOById(id: Long): FlatTaskDTO {
        return flatDTO(taskDAO.findDTOById(id))
    }

    fun findAllFlatTaskDTO(): MutableList<FlatTaskDTO> {
        return taskDAO.findAllDTO().map { taskDTO ->
            flatDTO(taskDTO)
        }.toMutableList()
    }


    private fun saveCopyCascade(e: FlatTaskDTO): FlatTaskDTO {
        // appends the copy to the parent and overwrites the ids
        // ignore the parent
        // overwrite subtask ids and there parents
        // save top to bottom

        GlobalScope.runCatching {

            e.label.name = "COPY"
            val rowIndex = createFromCopy(e)
            val all = findAllFlatTaskDTO()
            val new = findFlatTaskDTOById(rowIndex)

            // todo create labels before insert

//            val flattasks = flatFlatTaskDTO(e)
//            val taksFlat = flattasks.map { flatTaskDTO -> flatTaskDTO_ToTaks(flatTaskDTO) }
//
//            flatFlatTaskDTO(e).map { flatTaskDTO -> flatTaskDTO_ToTaks(flatTaskDTO) }.forEach {
//                // save
//            }


            // save the child

            //flatMap the subtask to a new list and don't bulk insert the id of each is needed

        }

        return e
    }

    private fun flatFlatTaskDTO(e: FlatTaskDTO): MutableList<FlatTaskDTO> {
        val tasks: MutableList<FlatTaskDTO> = mutableListOf()
        flattenFlatTaskDTO(e, tasks, 0)
        return tasks
    }

    private fun flattenFlatTaskDTO(e: FlatTaskDTO, tasks: MutableList<FlatTaskDTO>, id: Long) {
        val subTasks = e.subTasks
        if (subTasks.isEmpty()) {
            e.id = id
            tasks.add(e)
            return
        }
        subTasks.forEach {
            flattenFlatTaskDTO(it, tasks, id - 1)
        }
    }


    /**
     * transforms a flatTask to a list of flatTasks without the tree structure
     */
    private fun flattenToTasks(e: FlatTaskDTO, tasks: MutableList<FlatTaskDTO>) {
        val subTasks = e.subTasks
        if (subTasks.isEmpty()) {
            tasks.add(e)
            return
        }
        subTasks.forEach {
            flattenToTasks(it, tasks)
        }
    }

    fun deleteAsync(e: FlatTaskDTO) {
        GlobalScope.run {
            delete(e)
        }
    }

    private fun taskToFlatTaskDTO() = Function<MutableList<TaskDTO>, MutableList<FlatTaskDTO>> {
        it.map { taskDTO ->
            flatDTO(
                taskDTO
            )
        }.toMutableList()
    }

}
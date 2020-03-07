package to.freebots.todobutler.models.logic

import android.app.Application
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.functions.Function
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO
import java.time.LocalDateTime
import java.util.*

class FlatTaskService(
    application: Application,
    private val taskService: TaskService,
    private val attachmentService: AttachmentService
) :
    BaseLogicService<FlatTaskDTO>(application) {

    // todo add rx observer
    // todo add try catch and link to errorHandler

    val findAllFlatTasks: Flowable<MutableList<FlatTaskDTO>> =
        database.taskDAO().findAllDTOFlowable()
            .doOnError { t: Throwable? -> t?.let { errorChannel.onNext(it) } }
            .map(taskToFlatTaskDTO())

    override fun findAll(): MutableList<FlatTaskDTO> {
        return taskDAO.findAllDTO().map { taskDTO -> flatDTO(taskDTO) }.toMutableList()
    }

    override fun findById(id: Long): FlatTaskDTO {
//        var task = taskDAO.findAllDTO()
//        task = task.filter { task ->
//            if (task.task.id != null) {
//                return@filter task.task.id == id
//            }
//            false
//        }.toMutableList()
//        return flatDTO(task.first())
        var task = taskDAO.findByIdDTO1(id)

        return flatDTO(task)
    }

    fun findById(parent: Long?, id: Long): FlatTaskDTO {
        var task = taskDAO.findByIdDTO(parent, id)

        return flatDTO(task)
    }

    override fun create(e: FlatTaskDTO): FlatTaskDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: FlatTaskDTO): FlatTaskDTO {
        // todo update only the current parent and not the children
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO): FlatTaskDTO {
        // transform to the tasks
        val tasks: MutableList<FlatTaskDTO> = mutableListOf()
        flattenToTasks(e, tasks)

        val attachmentIds: List<Long> = tasks.flatMap { flatTaskDTO -> flatTaskDTO.attachments }
            .map { attachment -> attachment.id!! }.toList()
        attachmentService.deleteAllByIds(attachmentIds)

        taskService.deleteAllByIds(tasks.map { flatTaskDTO -> flatTaskDTO.id!! }.toMutableList())
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
            taskDTO.subTasks.map { task: Task -> flatDTO(taskDAO.findDTOById(task.id!!)) }
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
            e.label.id!!,
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
//    fun copyIntoParent(e: FlatTaskDTO) {
//        //todo overwrite ids
//        // add to parent
//        e.parentTaskId?.let {
//            val parent = findFlatTaskDTOById(it)
//            val copy =
//                (copy(e, FlatTaskDTO.javaClass) as FlatTaskDTO).apply { parentTaskId = parent.id }
//            saveCopyCascade(copy)
//        } ?: run {
//            // create a new task as parent
//            val copy = copy(e, FlatTaskDTO.javaClass) as FlatTaskDTO
//            saveCopyCascade(copy)
//        }
//    }


    fun copyIntoParent(e: FlatTaskDTO): Observable<FlatTaskDTO> {
        return Observable.fromCallable {
            e.description = UUID.randomUUID().toString() + LocalDateTime.now().toString()
            val copy = copy(e, FlatTaskDTO::class.java) as FlatTaskDTO
            val tasks: MutableList<FlatTaskDTO> =
                flatFlatTaskDTO(copy).sortedBy { flatTaskDTO -> flatTaskDTO.id }.toMutableList()

            var tasksEntities: MutableList<Task> =
                tasks.map { flatTaskDTO -> flatTaskDTO_ToTaks(flatTaskDTO) }.toMutableList()

            tasksEntities = removeIds(tasksEntities)


            val createdTasks = taskService.createAll(tasksEntities)
            createdTasks.forEachIndexed { index, task ->

                val attachments = tasks[index].attachments.map { attachment ->
                    attachment.taskId = task.id!!
                    attachment.id = null
                    attachment
                }.toMutableList()
                attachmentService.createAll(attachments)
            }

            findById(createdTasks.first().id!!)
        }
    }


    private fun tasksToDelete(e: FlatTaskDTO): MutableList<Long> {
        val ids: MutableList<Long> = mutableListOf()
        tasksToDelete(e, ids)
        ids.reverse()
        return ids
    }

    private fun tasksToDelete(e: FlatTaskDTO, ids: MutableList<Long>) {
        ids.add(e.id!!)

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

    fun createAsync(e: FlatTaskDTO): Observable<FlatTaskDTO> {
        // persists attachments
        // persists labels
        // persists tasks

        val tasks: MutableList<FlatTaskDTO> =
            flatFlatTaskDTO(e).sortedBy { flatTaskDTO -> flatTaskDTO.id }.toMutableList()

        var tasksEntities: MutableList<Task> =
            tasks.map { flatTaskDTO -> flatTaskDTO_ToTaks(flatTaskDTO) }.toMutableList()

        tasksEntities = removeIds(tasksEntities)

        return Observable.fromCallable {
            val createdTasks = taskService.createAll(tasksEntities)
            createdTasks.forEachIndexed { index, task ->
                val attachments = tasks[index].attachments.map { attachment ->
                    attachment.taskId = task.id!!
                    attachment
                }.toMutableList()
                attachmentService.updateAll(attachments)
            }
            findById(createdTasks.first().id!!)
        }
    }

    fun deleteAsync(e: FlatTaskDTO) {
        GlobalScope.run {
            delete(e)
        }
    }

    private fun removeIds(tasks: MutableList<Task>): MutableList<Task> {
        return tasks.map { task ->
            task.id = null
            task
        }.toMutableList()
    }

    private fun taskToFlatTaskDTO() = Function<MutableList<TaskDTO>, MutableList<FlatTaskDTO>> {
        it.map { taskDTO ->
            flatDTO(
                taskDTO
            )
        }.toMutableList()
    }


    fun test(id:Long): Observable<FlatTaskDTO> {
        return taskDAO.findDTOByIdTEST(id).map { t -> flatDTO(t) }.toObservable()
    }

    fun updateAsync(e: FlatTaskDTO) {
        taskDAO.update(flatTaskDTO_ToTaks(e))
    }
}
package to.freebots.todobutler.models.logic

import android.app.Application
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

class TaskService(application: Application, private val labelService: LabelService) :
    BaseLogicService<Task>(application) {
    // todo move functions from viewModel here

    private val _tasks = database.taskDAO().findAllLiveData()

    fun findAllTask() = _tasks

    fun findAllTaskObservable() = database.taskDAO().findAllDTOFlowable().toObservable()

    fun findAllFlatTaskDTO(): MutableList<FlatTaskDTO> {
        return taskDAO.findAllDTO().map { taskDTO ->
            flatDTO(taskDTO)
        }.toMutableList()
    }


    fun findFlatTaskDTOById(id: Long): FlatTaskDTO {
        return flatDTO(taskDAO.findDTOById(id))
    }

    fun delete(e: FlatTaskDTO) {
        val ids = tasksToDelete(e)
        GlobalScope.runCatching {
            ids.forEach {
                taskDAO.deleteById(it)
            }
        }
    }

    fun createFromCopy(flatTaskDTO: FlatTaskDTO): Long {
        return saveNewTasks(flatTaskDTO, flatTaskDTO.parentTaskId)
    }

    private fun saveNewTasks(e: FlatTaskDTO, id: Long?): Long {
        val subTasks = e.subTasks
        e.parentTaskId = id
        val labelIndex = labelService.create(e.label).id
        e.label.id = labelIndex
        val rowIndex = create(flatTaskDTO_ToTaks(e)).id
        subTasks.forEach {
            saveNewTasks(it, rowIndex)
        }
        return rowIndex
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

    override fun findAll(): MutableList<Task> = taskDAO.findAll()

    override fun findById(id: Long): Task = taskDAO.findById(id)

    override fun create(e: Task): Task {
        // todo get the new entity
        taskDAO.create(e)
        return e
    }

    override fun update(e: Task): Task {
        taskDAO.update(e)
        return e
    }

    override fun delete(e: Task): Task {
        taskDAO.delete(e)
        return e
    }
}
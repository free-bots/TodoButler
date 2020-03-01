package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Task

class TaskService(application: Application, private val labelService: LabelService) :
    BaseLogicService<Task>(application) {

    private val _tasks = database.taskDAO().findAllLiveData()

    fun findAllTask() = _tasks

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

    fun deleteAllByIds(tasks: MutableList<Long>) {
        taskDAO.deleteAllById(tasks)
    }
}
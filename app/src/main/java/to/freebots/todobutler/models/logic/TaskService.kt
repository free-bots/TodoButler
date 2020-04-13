package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Task

class TaskService(application: Application, private val labelService: LabelService) :
    BaseLogicService<Task>(application) {

    override fun findAll(): MutableList<Task> = taskDAO.findAll()

    override fun findById(id: Long): Task = taskDAO.findById(id)

    override fun create(e: Task): Task {
        return taskDAO.findByRowId(taskDAO.create(e))
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

    private fun findByRowId(id: Long): Task {
        return taskDAO.findByRowId(id)
    }

    fun createAll(e: MutableList<Task>): MutableList<Task> {
        return taskDAO.insert(e).map { l -> findByRowId(l) }.toMutableList()
    }

    fun updateAll(e: MutableList<Task>): Int {
        return taskDAO.updateAll(e)
    }
}
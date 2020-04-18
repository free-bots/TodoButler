package to.freebots.todobutler.models.logic

import android.app.Application
import androidx.lifecycle.LiveData
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Label

class LabelService(application: Application, var taskService: FlatTaskService?) : BaseLogicService<Label>(application) {

    private val _tasks: LiveData<MutableList<Label>> = labelDao.findAllLiveData()

    fun findAllLiveData(): LiveData<MutableList<Label>> = _tasks

    override fun findAll(): MutableList<Label> = labelDao.findAll()

    override fun findById(id: Long): Label = labelDao.findById(id)

    override fun create(e: Label): Label {
        val rowIndex = labelDao.create(e)
        return labelDao.findByRowIndex(rowIndex)
    }

    override fun update(e: Label): Label {
        labelDao.update(e)
        return e
    }

    override fun delete(e: Label): Label {
        e.id?.let {
            taskService?.deleteByLabelId(it)
        }
        labelDao.delete(e)
        return e
    }
}
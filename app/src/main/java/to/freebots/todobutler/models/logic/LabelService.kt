package to.freebots.todobutler.models.logic

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Label

class LabelService(application: Application) : BaseLogicService<Label>(application) {

    private val _tasks: LiveData<MutableList<Label>> = labelDao.findAllLiveData()

    fun findAllLiveData(): LiveData<MutableList<Label>> = _tasks

    override fun findAll(): MutableList<Label> = labelDao.findAll()

    override fun findById(id: Long): Label = labelDao.findById(id)

    fun createAsync(e: Label) {
        GlobalScope.run {
            labelDao.create(e)
        }
    }

    override fun create(e: Label): Label {
        val rowIndex = labelDao.create(e)
        return labelDao.findByRowIndex(rowIndex)
    }

    fun updateAsync(e: Label) {
        GlobalScope.run {
            labelDao.update(e)
        }
    }

    override fun update(e: Label): Label {
        labelDao.update(e)
        return e
    }

    fun deleteAsync(e: Label) {
        GlobalScope.run {
            labelDao.delete(e)
        }
    }

    override fun delete(e: Label): Label {
        labelDao.delete(e)
        return e
    }
}
package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task

class TaskViewModel(application: Application) : BaseViewModel(application), BaseOperations<Task>, BaseFlatTaskOperations {

    val tasks: MutableLiveData<MutableList<FlatTaskDTO>> = MutableLiveData()
    private var _tasks: MutableList<FlatTaskDTO> = mutableListOf()
    private var _filteredTasks: MutableList<FlatTaskDTO> = mutableListOf()

    override fun fetchAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(e: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetchAll_DTO() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(e: FlatTaskDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: FlatTaskDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun filterByLabel(label: Label) {
        _filteredTasks = _tasks.filter { l -> l.id == label.id }.toMutableList()
        // notify
    }
}
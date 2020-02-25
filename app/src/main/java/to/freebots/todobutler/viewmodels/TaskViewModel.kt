package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task

class TaskViewModel(application: Application) : BaseViewModel(application), BaseOperations<Task>,
    BaseFlatTaskOperations {

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
        _tasks.add(e)
        tasks.postValue(_tasks)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: FlatTaskDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO) {

        val ids = tasksToDelete(e)
        GlobalScope.runCatching {
            ids.forEach {
                taskDAO.deleteById(it)
            }
        }
    }

    fun filterByLabel(label: Label) {
        _filteredTasks = _tasks.filter { l -> l.id == label.id }.toMutableList()
        // notify
    }

    /**
     * makes a copy of the current task and adds it as a child to the parent task
     */
    fun copyIntoParrent(e: FlatTaskDTO) {
        //todo overwrite ids
        // add to parent
        e.parentTaskId?.let {
            val parent = taskDAO.findFlatTaskDTOById(it)
            val copy = copy(e).apply { parentTaskId = parent.id }
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

    private fun copy(e: FlatTaskDTO): FlatTaskDTO {
        val json = Gson().toJson(e)
        return Gson().fromJson<FlatTaskDTO>(json, FlatTaskDTO::class.java)
    }


    private fun saveCopyCascade(e: FlatTaskDTO): FlatTaskDTO {
        // appends the copy to the parent and overwrites the ids
        // ignore the parent
        // overwrite subtask ids and there parents
        // save top to bottom

        GlobalScope.runCatching {

            flatFlatTaskDTO(e).map { flatTaskDTO -> tasksToDelete(flatTaskDTO) }.forEach {
                // save
            }
            // save the child

            //flatMap the subtask to a new list and don't bulk insert the id of each is needed

        }

        return e
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

    private fun flatFlatTaskDTO(e: FlatTaskDTO): MutableList<FlatTaskDTO> {
        return mutableListOf()
    }
}
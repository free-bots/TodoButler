package to.freebots.todobutler.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.ComputableLiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.logic.FlatTaskService
import to.freebots.todobutler.models.logic.LabelService
import to.freebots.todobutler.models.logic.TaskService

class TaskViewModel(application: Application) : BaseViewModel(application), BaseOperations<Task>,
    BaseFlatTaskOperations {

    private val taskService by lazy {
        TaskService(application, LabelService(application))
    }

    private val flatTaskService by lazy {
        FlatTaskService(application)
    }

    private val errorHandler = Consumer<Throwable> { t ->
        // todo show error message in ui
        Log.e(this.javaClass.canonicalName, t.message, t)
    }

    private val onFlatTaskDTO = Consumer<Any> { t: Any ->
        when (t) {
            is MutableList<*> -> {
                _flatTasks.postValue(t as MutableList<FlatTaskDTO>)
            }
        }
    }

    init {
        subscribe(
            applyBackgroundScheduler(flatTaskService.flatTaskObservable.toObservable()).subscribe(
                onFlatTaskDTO, errorHandler
            )
        )
    }

    val tasks = taskService.findAllTask()
    private val _flatTasks = MutableLiveData<MutableList<FlatTaskDTO>>()


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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: FlatTaskDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO) {

    }

    fun filterByLabel(label: Label) {
        _filteredTasks = _tasks.filter { l -> l.id == label.id }.toMutableList()
        // notify
    }

    /**
     * makes a copy of the current task and adds it as a child to the parent task
     */
    fun copyIntoParent(e: FlatTaskDTO) {

    }
}
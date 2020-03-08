package to.freebots.todobutler.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import to.freebots.todobutler.common.Event
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.logic.*

class TaskViewModel(application: Application) : BaseViewModel(application), BaseOperations<Task>,
    BaseFlatTaskOperations {

    // todo swipeRefresh layout....
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    val isEditing: MutableLiveData<Boolean> = MutableLiveData()

    private var _current: FlatTaskDTO? = null

    // flatTaskDTO editable fields
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val isCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val subTasks: MutableLiveData<MutableList<FlatTaskDTO>> = MutableLiveData()


    // navigate on new subTasks, clone ....
    val navigate: MutableLiveData<Event<FlatTaskDTO>> = MutableLiveData()


    val a: MutableLiveData<String> = MutableLiveData("HI")

    val _task: MutableLiveData<FlatTaskDTO> = MutableLiveData()

    private var _filterLabel: Label? = null

    private val taskService by lazy {
        TaskService(application, LabelService(application))
    }

    private val flatTaskService by lazy {
        FlatTaskService(
            application,
            taskService,
            AttachmentService(application, StorageService((application)))
        )
    }

    private val errorHandler = Consumer<Any> { t ->
        // todo show error message in ui
        when (t) {
            is Throwable -> {
                Log.e(this.javaClass.canonicalName, t.message, t)
            }
        }
    }

    private val onFlatTaskDTO = Consumer<Any> { t: Any ->
        when (t) {
            is MutableList<*> -> {
                val tasks: MutableList<FlatTaskDTO> = t as MutableList<FlatTaskDTO>
                _flatTasks.postValue(tasks)
            }
        }
    }

    private fun filterByLabel(
        label: Label?,
        tasks: MutableList<FlatTaskDTO>
    ): MutableList<FlatTaskDTO> {
        return tasks.filter { flatTaskDTO ->
            val flatDTOLabelId = flatTaskDTO.label.id
            if (label?.id != null && flatDTOLabelId != null) {
                return@filter flatDTOLabelId == label.id
            }
            true
        }.toMutableList()
    }

    fun filterByLabel(tasks: MutableList<FlatTaskDTO>): MutableList<FlatTaskDTO> =
        this.filterByLabel(_filterLabel, tasks)

    init {
        subscribe(
            applyBackgroundScheduler(flatTaskService.findAllFlatTasks.toObservable()).subscribe(
                onFlatTaskDTO, errorHandler
            )
        )
        subscribe(
            applyBackgroundScheduler(
                BaseLogicService.errorChannel.toFlowable(
                    BackpressureStrategy.MISSING
                ).toObservable()
            ).subscribe(errorHandler)
        )
    }

    private val _flatTasks = MutableLiveData<MutableList<FlatTaskDTO>>()

    val flatTasks = _flatTasks

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
        flatTaskService.updateAsync(e)
//        // todo only update the top task in the tree -> child update self
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: FlatTaskDTO) {
        flatTaskService.deleteAsync(e)
    }

    fun filterByLabel(label: Label) {
        _filterLabel = label
        // notify
    }

    fun update() {
        _current?.name = name.value!!
        _current?.description = description.value!!
        _current?.isCompleted = isCompleted.value!!
        update(_current!!)
        isEditing.postValue(false)
    }

    /**
     * makes a copy of the current task and adds it as a child to the parent task
     */
    fun copyIntoParent(){
     subscribe(flatTaskService.copyIntoParent(_current!!).subscribe{
         t: FlatTaskDTO? ->
         t?.let {
             navigate(t)
         }
     })
    }


    fun createSubTask() {

        if (_current != null) {
            val default = FlatTaskDTO(
                _current!!.label,
                _current!!.id,
                "NEW",
                "DESC ${_current!!.id}",
                false,
                mutableListOf(),
                mutableListOf(),
                null
            )

            subscribe(flatTaskService.createAsync(default).subscribe{t: FlatTaskDTO? ->
                t?.let {
                    navigate(t)
                }
            })
        } else {
            throw Exception("no task found")
        }


    }

    fun navigate(e: FlatTaskDTO) {
        // navigate and cleanup
        _current = e
        navigate.postValue(Event(e))
    }

    fun createDefaultFlatTask(label: Label): Observable<FlatTaskDTO> {
        val default =
            FlatTaskDTO(label, null, "NEW", "DESC", false, mutableListOf(), mutableListOf(), null)
        return flatTaskService.createAsync(default).doOnError(errorHandler)
    }


    fun task(id: Long): LiveData<FlatTaskDTO> {
        if (_task.value == null) {
            val task = flatTaskService.findById(id)
            _task.postValue(task)
        }
        return _task
    }

    fun getUpdated(id: Long) {
        flatTaskService.test(id).subscribe{t: FlatTaskDTO? -> t?.let {
            apply(t)
            _task.postValue(t)
        } }
    }

    private fun apply(flatTaskDTO: FlatTaskDTO) {
        _current = flatTaskDTO
        name.postValue(flatTaskDTO.name)
        description.postValue(flatTaskDTO.description)
        isCompleted.postValue(flatTaskDTO.isCompleted)
        subTasks.postValue(flatTaskDTO.subTasks)
    }

    fun TEST(): LiveData<FlatTaskDTO> = _task
}
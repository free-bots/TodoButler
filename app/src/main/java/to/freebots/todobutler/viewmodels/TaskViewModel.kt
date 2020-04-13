package to.freebots.todobutler.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import to.freebots.todobutler.common.Event
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.*
import to.freebots.todobutler.models.logic.*
import java.util.*

class TaskViewModel(application: Application) : BaseViewModel(application) {

    private val locationService: LocationService by lazy {
        LocationService(application)
    }

    private val reminderService: ReminderService by lazy {
        ReminderService(application)
    }

    // todo swipeRefresh layout....
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    val isEditing: MutableLiveData<Boolean> = MutableLiveData()

    var isDeleting: MutableLiveData<Boolean> = MutableLiveData(false)

    private var _current: FlatTaskDTO? = null

    // flatTaskDTO editable fields
    val name: MutableLiveData<String> = MutableLiveData()
    val description: MutableLiveData<String> = MutableLiveData()
    val isCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val isPinned: MutableLiveData<Boolean> = MutableLiveData()
    val subTasks: MutableLiveData<MutableList<FlatTaskDTO>> = MutableLiveData()
    val color: MutableLiveData<String> = MutableLiveData()
    val location: MutableLiveData<Location?> = MutableLiveData()
    val reminder: MutableLiveData<Reminder?> = MutableLiveData()
    val priority: MutableLiveData<Priority> = MutableLiveData()

    // navigate on new subTasks, clone ....
    val navigate: MutableLiveData<Event<EventWrapper<FlatTaskDTO>>> = MutableLiveData()

    val labelIcon: MutableLiveData<Int> = MutableLiveData(0)

    val _task: MutableLiveData<FlatTaskDTO> = MutableLiveData()

    private var _filterLabel: Label? = null

    private val taskService by lazy {
        TaskService(application, LabelService(application))
    }

    private val flatTaskService by lazy {
        FlatTaskService(
            application,
            taskService,
            AttachmentService(application, StorageService((application))),
            LocationService(application),
            ReminderService(application)
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

    @Suppress("UNCHECKED_CAST")
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

    val searchedTasks: MutableLiveData<MutableList<FlatTaskDTO>> = MutableLiveData()
    var searchQuery: MutableLiveData<String> = MutableLiveData()

    @SuppressLint("DefaultLocale")
    fun search(query: String) {
        val trimQuery = query.trim().toLowerCase()

        if (trimQuery.isEmpty()) {
            this.searchedTasks.postValue(mutableListOf())
            return
        }

        val gson = Gson()

        this._flatTasks.value?.let {
            this.searchedTasks.postValue(
                it.filter { task ->
                    val str = gson.toJson(task).toLowerCase()
                    str.contains(trimQuery)
                }.toMutableList()
            )
        }
    }

    fun delete() {
        this.isDeleting.postValue(true)
        subscribe(
            flatTaskService.deleteRx(_current!!)
                .subscribe({
                    this.isDeleting.postValue(false)
                    navigate(null, true)
                }, {
                    this.isDeleting.postValue(false)
                })
        )
    }

    fun filterByLabel(label: Label) {
        _filterLabel = label
        // notify
    }

    fun update() {
        subscribe(updateCurrent()?.subscribe {})
    }

    private fun updateCurrent(): Observable<Task>? {
        if (isDeleting.value ?: run { false }) {
            return null
        }

        if (_current == null) {
            return null
        }

        _current?.name = name.value!!
        _current?.description = description.value!!
        _current?.isCompleted = isCompleted.value!!
        _current?.isPinned = isPinned.value!!
        _current?.color = color.value!!
        _current?.location = location.value
        _current?.reminder = reminder.value
        _current?.priority = priority.value!!

        return flatTaskService.updateRxAsTask(_current!!)
            .doOnNext {
                isEditing.postValue(false)
            }
    }

    /**
     * makes a copy of the current task and adds it as a child to the parent task
     */
    fun copyIntoParent() {
        val updateObservable = updateCurrent()

        if (updateObservable != null) {
            subscribe(
                updateObservable.flatMap { flatTaskService.copyIntoParent(_current!!) }
                    .subscribe { t: FlatTaskDTO? ->
                        t?.let {
                            navigate(t)
                        }
                    })
        } else {
            subscribe(flatTaskService.copyIntoParent(_current!!).subscribe { t: FlatTaskDTO? ->
                t?.let {
                    navigate(t)
                }
            })
        }
    }


    fun createSubTask() {

        if (_current == null) {
            throw Exception("no task found")
        }


        val default = FlatTaskDTO(
            _current!!.label,
            _current!!.id,
            "NEW",
            "DESC ${_current!!.id}",
            false,
            false,
            null,
            null,
            Priority.LOW,
            _current!!.color,
            mutableListOf(),
            mutableListOf(),
            null
        )


        val updateObservable = updateCurrent()

        if (updateObservable != null) {
            subscribe(
                updateObservable.flatMap { flatTaskService.createRx(default) }
                    .subscribe { t: FlatTaskDTO? ->
                        t?.let {
                            navigate(t)
                        }
                    })
        } else {
            subscribe(flatTaskService.createRx(default).subscribe { t: FlatTaskDTO? ->
                t?.let {
                    navigate(t)
                }
            })
        }
    }

    fun navigate(e: FlatTaskDTO?, navigateUp: Boolean = false) {
        // navigate and cleanup
        _current = e
        navigate.postValue(Event(EventWrapper(e, navigateUp)))
    }

    fun createDefaultFlatTask(label: Label): Observable<FlatTaskDTO> {
        val default =
            FlatTaskDTO(
                label,
                null,
                "NEW",
                "DESC",
                false,
                false,
                null,
                null,
                Priority.LOW,
                "",
                mutableListOf(),
                mutableListOf(),
                null
            )
        return flatTaskService.createRx(default).doOnError(errorHandler)
    }


    fun task(id: Long): LiveData<FlatTaskDTO> {
        if (_task.value == null) {
            val task = flatTaskService.findById(id)
            _task.postValue(task)
        }
        return _task
    }

    fun getUpdated(id: Long) {
        subscribe(flatTaskService.test(id)
            .subscribe { t: FlatTaskDTO? ->
                t?.let {
                    apply(t)
                    _task.postValue(t)
                }
            })
    }

    private fun apply(flatTaskDTO: FlatTaskDTO) {
        _current = flatTaskDTO
        name.postValue(flatTaskDTO.name)
        description.postValue(flatTaskDTO.description)
        isCompleted.postValue(flatTaskDTO.isCompleted)
        isPinned.postValue(flatTaskDTO.isPinned)
        subTasks.postValue(flatTaskDTO.subTasks)
        color.postValue(flatTaskDTO.color)
        location.postValue(flatTaskDTO.location)
        reminder.postValue(flatTaskDTO.reminder)
        priority.postValue(flatTaskDTO.priority)

        if (flatTaskDTO.label.icon.isEmpty().not()) {
            labelIcon.postValue(flatTaskDTO.label.icon.toInt())
        }
    }

    fun updateReminderDate(year: Int, month: Int, dayOfMonth: Int) {
        if (this.reminder.value == null) {

            val reminder = Reminder(Date(), null, null, null, null)

            subscribe(
                reminderService.createRx(
                    updateDateInReminder(
                        year,
                        month,
                        dayOfMonth,
                        reminder
                    )
                ).subscribe {
                    this.reminder.value = it
                    this.update()
                })
        } else {
            subscribe(
                reminderService.updateRx(
                    updateDateInReminder(
                        year,
                        month,
                        dayOfMonth,
                        this.reminder.value!!
                    )
                ).subscribe {
                    this.reminder.value = it
                    this.update()
                })
        }


    }

    private fun updateDateInReminder(
        year: Int,
        month: Int,
        dayOfMonth: Int,
        reminder: Reminder
    ): Reminder {
        val calendar = Calendar.getInstance()
        calendar.time = reminder.date

        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        reminder.date = calendar.time

        return reminder
    }

    fun updateReminderTime(hourOfDay: Int, minute: Int) {
        if (this.reminder.value == null) {
            val reminder = Reminder(Date(), null, null, null, null)
            subscribe(
                reminderService.createRx(
                    updateTimeInReminder(
                        hourOfDay,
                        minute,
                        reminder
                    )
                ).subscribe {
                    this.reminder.value = it
                    this.update()
                })
        } else {
            subscribe(
                reminderService.updateRx(
                    updateTimeInReminder(hourOfDay, minute, this.reminder.value!!)
                ).subscribe {
                    this.reminder.value = it
                    this.update()
                })
        }
    }

    private fun updateTimeInReminder(hourOfDay: Int, minute: Int, reminder: Reminder): Reminder {
        val calendar = Calendar.getInstance()
        calendar.time = reminder.date

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        calendar.set(Calendar.MINUTE, minute)

        reminder.date = calendar.time

        return reminder
    }

    fun deleteReminder() {
        this.reminder.value?.let {
            subscribe(reminderService.deleteRx(it).subscribe {
                this.reminder.postValue(null)
            })
        }
    }

    fun deleteLocation() {
        this.location.value?.let {
            subscribe(locationService.deleteRx(it).subscribe {
                this.location.value = null
                this.update()
            })
        }
    }

    fun updateLocation(latitude: Double, longitude: Double) {

        if (location.value == null) {

            subscribe(locationService.createRx(
                Location(
                    latitude,
                    longitude
                )
            ).subscribe {
                this.location.value = it
                this.update()
            })

        } else {

            this.location.value?.let {
                it.latitude = latitude
                it.longitude = longitude
                subscribe(locationService.updateRx(
                    it
                ).subscribe { updatedLocation ->
                    this.location.value = updatedLocation
                    this.update()
                })
            }

        }

    }

    open class EventWrapper<E>(var e: E?, val navigateUp: Boolean = false)
}
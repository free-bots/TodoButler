package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.database.Database
import to.freebots.todobutler.models.logic.*

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    // todo add the logic services with dependency injection
    private val disposable = CompositeDisposable()

    protected var database = Database.getDatabase(application)

    protected val labelDao by lazy {
        database.labelDAO()
    }

    protected val attachmentDAO by lazy {
        database.attachmentDAO()
    }

    protected val taskDAO by lazy {
        database.taskDAO()
    }

    protected fun findIndex(baseEntity: BaseEntity, baseEntities: MutableList<BaseEntity>): Int {
        return baseEntities.indexOfFirst { b -> b.id == baseEntity.id }
    }

    protected fun applyBackgroundScheduler(observable: Observable<*>): Observable<*> {
        return observable
            .observeOn(Schedulers.computation())
            .subscribeOn(Schedulers.computation())
    }

    protected fun subscribe(disposable: Disposable?) {
        if (disposable == null) {
            return
        }

        this.disposable.add(disposable)
    }

    override fun onCleared() {
        disposable.dispose()
        super.onCleared()
    }

    val locationService = LocationService(application)
    val storageService = StorageService(application)
    val reminderService = ReminderService(application)
    val attachmentService = AttachmentService(application, storageService)
    val labelService = LabelService(application, null)
    val taskService = TaskService(application, labelService)
    val flatTaskService = FlatTaskService(
        application,
        taskService,
        attachmentService,
        locationService,
        reminderService
    )

    init {
        labelService.taskService = flatTaskService
    }
}
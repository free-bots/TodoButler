package to.freebots.todobutler.common.logic

import android.app.Application
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.database.Database
import to.freebots.todobutler.models.entities.FlatTaskDTO

abstract class BaseLogicService<E : BaseEntity>(application: Application) {

    companion object {
        // use a general handler for every error in the subclasses
        val errorChannel: Subject<Throwable> = PublishSubject.create()
    }

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

    protected val reminderDAO by lazy {
        database.reminderDAO()
    }

    protected val locationDAO by lazy {
        database.locationDAO()
    }

    protected fun findIndex(baseEntity: BaseEntity, baseEntities: MutableList<BaseEntity>): Int {
        return baseEntities.indexOfFirst { b -> b.id == baseEntity.id }
    }

    protected fun copy(e: Any, objClass: Class<*>): Any {
        val json = Gson().toJson(e)
        return Gson().fromJson<FlatTaskDTO>(json, objClass)
    }

    abstract fun findAll(): MutableList<E>
    abstract fun findById(id: Long): E
    abstract fun create(e: E): E
    abstract fun update(e: E): E
    abstract fun delete(e: E): E

    open fun createRx(e: E): Observable<E> {
        return Observable.fromCallable {
            create(e)
        }
    }

    open fun updateRx(e: E): Observable<E> {
        return Observable.fromCallable {
            update(e)
        }
    }

    open fun deleteRx(e: E): Observable<E> {
        return Observable.fromCallable {
            delete(e)
        }
    }
}
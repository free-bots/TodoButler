package to.freebots.todobutler.common.logic

import android.app.Application
import com.google.gson.Gson
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.database.Database
import to.freebots.todobutler.models.entities.FlatTaskDTO

abstract class BaseLogicService<E : BaseEntity>(application: Application) {

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

    protected fun findIndex(baseEntity: BaseEntity, baseEntitys: MutableList<BaseEntity>): Int {
        return baseEntitys.indexOfFirst { b -> b.id == baseEntity.id }
    }

    protected fun copy(e: Any, objClass: Class<Any>): Any {
        val json = Gson().toJson(e)
        return Gson().fromJson<FlatTaskDTO>(json, objClass)
    }

    abstract fun findAll(): MutableList<E>
    abstract fun findById(id: Long): E
    abstract fun create(e: E): E
    abstract fun update(e: E): E
    abstract fun delete(e: E): E
}
package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import to.freebots.todobutler.common.entities.BaseEntity
import to.freebots.todobutler.models.database.Database

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
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
}
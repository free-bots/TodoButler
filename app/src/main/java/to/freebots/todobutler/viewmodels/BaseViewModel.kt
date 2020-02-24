package to.freebots.todobutler.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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
}
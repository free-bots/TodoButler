package to.freebots.todobutler.models.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Flowable
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Task
import to.freebots.todobutler.models.entities.TaskDTO

@Dao
abstract class TaskDAO : BaseDAO<Task> {

    @Query("SELECT * from Task")
    abstract fun findAll(): MutableList<Task>

    @Query("SELECT * from Task")
    abstract fun findAllLiveData(): LiveData<MutableList<Task>>

    @Query("SELECT * FROM Task WHERE id=:id")
    abstract fun findById(id: Long): Task

    @Transaction
    @Query("SELECT * from Task WHERE parentTaskId ISNULL")
    abstract fun findAllDTO(): MutableList<TaskDTO>

    @Transaction
    @Query("SELECT * from Task WHERE parentTaskId ISNULL")
    abstract fun findAllDTOLiveData(): LiveData<MutableList<TaskDTO>>

    @Transaction
    @Query("SELECT * from Task WHERE parentTaskId ISNULL")
    abstract fun findAllDTOFlowable(): Flowable<MutableList<TaskDTO>>

    @Transaction
    @Query("SELECT * from Task WHERE id=:id")
    abstract fun findDTOById(id: Long): TaskDTO

    @Insert
    abstract fun insert(tasks: MutableList<Task>)

    @Query("DELETE FROM Task WHERE id=:id")
    abstract fun deleteById(id: Long)

    @Query("SELECT * FROM Task WHERE rowid=:rowId")
    abstract fun findByRowId(rowId: Long): Task

    @Transaction
    open fun deleteAllById(ids: List<Long>) {
        ids.forEach { deleteById(it) }
    }
}

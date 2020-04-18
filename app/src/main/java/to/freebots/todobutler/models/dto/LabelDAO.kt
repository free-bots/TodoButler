package to.freebots.todobutler.models.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Label

@Dao
abstract class LabelDAO : BaseDAO<Label>() {

    @Query("SELECT * from Label")
    abstract fun findAllLiveData(): LiveData<MutableList<Label>>

    @Query("SELECT * from Label")
    abstract fun findAll(): MutableList<Label>

    @Query("SELECT * FROM Label WHERE id=:id")
    abstract fun findById(id: Long): Label

    @Query("SELECT * FROM LABEL WHERE rowid=:rowIndex")
    abstract fun findByRowIndex(rowIndex: Long): Label
}

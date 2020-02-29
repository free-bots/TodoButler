package to.freebots.todobutler.models.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Label

@Dao
interface LabelDAO : BaseDAO<Label> {

    @Query("SELECT * from Label")
    fun findAllLiveData(): LiveData<MutableList<Label>>

    @Query("SELECT * from Label")
    fun findAll(): MutableList<Label>

    @Query("SELECT * FROM Label WHERE id=:id")
    fun findById(id: Long): Label

    @Query("SELECT * FROM LABEL WHERE rowid=:rowIndex")
    fun findByRowIndex(rowIndex: Long): Label
}

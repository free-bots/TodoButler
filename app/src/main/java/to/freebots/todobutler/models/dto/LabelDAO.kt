package to.freebots.todobutler.models.dto

import androidx.room.Dao
import androidx.room.Query
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Label

@Dao
interface LabelDAO:BaseDAO<Label> {

    @Query("SELECT * from Label")
    fun findAll(): MutableList<Label>

    @Query("SELECT * FROM Label WHERE id=:id")
    fun findById(id: Long): Label
}

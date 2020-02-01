package to.freebots.todobutler.models.dto

import androidx.room.Dao
import androidx.room.Query
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Attachment

@Dao
interface AttachmentDAO : BaseDAO<Attachment> {

    @Query("SELECT * from Attachment")
    fun findAll(): MutableList<Attachment>

    @Query("SELECT * FROM Attachment WHERE id=:id")
    fun findById(id: Long): Attachment
}

package to.freebots.todobutler.models.dto

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Attachment

@Dao
abstract class AttachmentDAO : BaseDAO<Attachment>() {

    @Query("SELECT * from Attachment")
    abstract fun findAll(): MutableList<Attachment>

    @Query("SELECT * FROM Attachment WHERE id=:id")
    abstract fun findById(id: Long): Attachment

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    abstract fun findAllByTaskId(taskId: Long): MutableList<Attachment>

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    abstract fun findAllByTaskIdFlowable(taskId: Long): Flowable<MutableList<Attachment>>

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    abstract fun findAllByTaskIdLiveData(taskId: Long): LiveData<MutableList<Attachment>>

    @Query("SELECT * FROM Attachment WHERE rowid=:rowIndex")
    abstract fun findByRowIndex(rowIndex: Long): Attachment

    @Insert
    abstract fun createAll(attachments: MutableList<Attachment>): MutableList<Long>

    @Update
    abstract fun updateAll(attachments: MutableList<Attachment>): Int

    @Delete
    abstract fun deleteAll(attachments: MutableList<Attachment>)
}

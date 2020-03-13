package to.freebots.todobutler.models.dto

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Attachment

@Dao
interface AttachmentDAO : BaseDAO<Attachment> {

    @Query("SELECT * from Attachment")
    fun findAll(): MutableList<Attachment>

    @Query("SELECT * FROM Attachment WHERE id=:id")
    fun findById(id: Long): Attachment

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    fun findAllByTaskId(taskId: Long): MutableList<Attachment>

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    fun findAllByTaskIdFlowable(taskId: Long): Flowable<MutableList<Attachment>>

    @Query("SELECT * FROM ATTACHMENT WHERE taskId=:taskId")
    fun findAllByTaskIdLiveData(taskId: Long): LiveData<MutableList<Attachment>>

    @Query("SELECT * FROM Attachment WHERE rowid=:rowIndex")
    fun findByRowIndex(rowIndex: Long): Attachment

    @Insert
    fun createAll(attachments: MutableList<Attachment>): MutableList<Long>
}

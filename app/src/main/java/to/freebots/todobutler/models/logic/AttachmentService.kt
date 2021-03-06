package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import io.reactivex.Observable
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Attachment

class AttachmentService(application: Application, private val storageService: StorageService) :
    BaseLogicService<Attachment>(application) {

    override fun findAll(): MutableList<Attachment> = attachmentDAO.findAll()

    override fun findById(id: Long): Attachment = attachmentDAO.findById(id)

    override fun create(e: Attachment): Attachment {
        val rowIndex = attachmentDAO.create(e)
        return attachmentDAO.findByRowIndex(rowIndex)
    }

    fun createAll(e: MutableList<Attachment>): MutableList<Attachment> {
        val paths =
            storageService.makeCopyOfAllFiles(e.map { attachment -> attachment.path }.toMutableList())
        val copy = e.mapIndexed { index, attachment ->
            attachment.id = null
            attachment.path = paths[index]
            attachment
        }.toMutableList()

        return attachmentDAO.createAll(copy).map { attachmentDAO.findByRowIndex(it) }
            .toMutableList()
    }

    override fun update(e: Attachment): Attachment {
        attachmentDAO.update(e)
        return e
    }

    fun updateAll(e: MutableList<Attachment>): MutableList<Attachment> {
        attachmentDAO.updateAll(e)
        return e
    }

    override fun delete(e: Attachment): Attachment {
        attachmentDAO.delete(e)
        storageService.removeFile(e.path)
        return e
    }

    fun deleteAll(attachments: MutableList<Attachment>) {
        attachments.forEach {
            storageService.removeFile(it.path)
        }
        attachmentDAO.deleteAll(attachments)
    }

    fun createRx1(uri: Uri, taskId: Long): Observable<Attachment> {
        return Observable.fromCallable {
            val originalName = storageService.fileName(uri)
            val innerFileName = storageService.saveFile(uri)
            val extension = storageService.fileExtension(uri)
            create(Attachment(taskId, originalName, innerFileName, extension))
        }
    }

    fun findAllByTaskId(id: Long): Observable<MutableList<Attachment>> {
        return attachmentDAO.findAllByTaskIdFlowable(id).toObservable()
    }

    /**
     * size in bytes
     */
    fun attachmentSizeOfTask(taskId: Long): Long {
        return attachmentDAO.findAllByTaskId(taskId).map {
            storageService.file(it.path).length()
        }.sum()
    }

    fun copyPossible(taskId: Long): Boolean =
        storageService.freeSpace() > (attachmentSizeOfTask(taskId) * StorageService.SPACE_BUFFER)


}
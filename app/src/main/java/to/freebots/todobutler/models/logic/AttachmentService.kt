package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Attachment

class AttachmentService(application: Application, private val storageService: StorageService) : BaseLogicService<Attachment>(application) {

    override fun findAll(): MutableList<Attachment> = attachmentDAO.findAll()

    override fun findById(id: Long): Attachment = attachmentDAO.findById(id)

    override fun create(e: Attachment): Attachment {
        val rowIndex = attachmentDAO.create(e)
        return attachmentDAO.findByRowIndex(rowIndex)
    }

    fun createAll(e: MutableList<Attachment>): MutableList<Attachment> {
        //todo call file service to copy files
//        storageService.saveAllFiles(e.map { attachment -> Uri.parse(attachment.path) }.toMutableList())
        return e
    }

    override fun update(e: Attachment): Attachment {
        attachmentDAO.update(e)
        return e
    }

    fun updateAll(e: MutableList<Attachment>): MutableList<Attachment> {
        // todo db request
        return e
    }

    override fun delete(e: Attachment): Attachment {
        attachmentDAO.delete(e)
        return e
    }

    fun deleteAllByIds(id: List<Long>) {

    }
}
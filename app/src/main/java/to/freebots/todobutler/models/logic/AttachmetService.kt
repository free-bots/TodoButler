package to.freebots.todobutler.models.logic

import android.app.Application
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Attachment

class AttachmetService(application: Application) : BaseLogicService<Attachment>(application) {

    override fun findAll(): MutableList<Attachment> = attachmentDAO.findAll()

    override fun findById(id: Long): Attachment = attachmentDAO.findById(id)

    override fun create(e: Attachment): Attachment {
        val rowIndex = attachmentDAO.create(e)
        return attachmentDAO.findByRowIndex(rowIndex)
    }

    override fun update(e: Attachment): Attachment {
        attachmentDAO.update(e)
        return e
    }

    override fun delete(e: Attachment): Attachment {
        attachmentDAO.delete(e)
        return e
    }

    fun deleteAllByIds(id: List<Long>) {

    }
}
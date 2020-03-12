package to.freebots.todobutler.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.logic.AttachmentService
import to.freebots.todobutler.models.logic.StorageService

class AttachmentViewModel(application: Application) : BaseViewModel(application),
    BaseOperations<Attachment> {

    private val attachmentService: AttachmentService =
        AttachmentService(application, StorageService(application))

    private val _attachments: MutableLiveData<MutableList<Attachment>> = MutableLiveData()

    val attachments: LiveData<MutableList<Attachment>> = _attachments

    fun importFile(uri: Uri?, taskId: Long) {
        if (uri == null) {
            return
        }

        subscribe(attachmentService.createRx(uri, taskId).subscribe { attachment: Attachment ->
            println(Gson().toJson(attachment))
        })
    }

    override fun fetchAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(e: Attachment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(e: Attachment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(e: Attachment) {
        subscribe(attachmentService.deleteRx(e).subscribe {})
    }

    fun getAttachmentsByTaskId(id: Long) {
        subscribe(attachmentService.findAllByTaskId(id).subscribe { attachments: MutableList<Attachment> ->
            _attachments.postValue(attachments)
        })
    }

}
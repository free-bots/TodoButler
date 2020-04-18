package to.freebots.todobutler.viewmodels

import android.app.Application
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.reactivex.Observable
import to.freebots.todobutler.common.Event
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.logic.StorageService
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class AttachmentViewModel(application: Application) : BaseViewModel(application) {

    var selectedAttachment: MutableLiveData<Attachment> = MutableLiveData()

    val uri: MutableLiveData<Event<Uri>> = MutableLiveData()

    private val _attachments: MutableLiveData<MutableList<Attachment>> = MutableLiveData()

    val attachments: LiveData<MutableList<Attachment>> = _attachments

    fun importFile(uri: Uri?, taskId: Long) {
        if (uri == null) {
            return
        }

        subscribe(attachmentService.createRx1(uri, taskId).subscribe { attachment: Attachment ->
            println(Gson().toJson(attachment))
        })
    }

    fun delete(e: Attachment) {
        subscribe(attachmentService.deleteRx(e).subscribe {})
    }

    fun getAttachmentsByTaskId(id: Long) {
        subscribe(attachmentService.findAllByTaskId(id).subscribe { attachments: MutableList<Attachment> ->
            _attachments.postValue(attachments)
        })
    }

    fun createFile(uri: Uri) {
        subscribe(Observable.fromCallable {
            try {
                val application = getApplication<Application>()
                application.contentResolver?.openFileDescriptor(uri, "w")?.use {
                    FileOutputStream(it.fileDescriptor).use { fileOutputStream ->
                        val path = selectedAttachment.value?.path
                        fileOutputStream.write(StorageService(application).file(path!!).readBytes())
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            0
        }.subscribe { })
    }

    fun getUri(attachment: Attachment) {
        subscribe(Observable.fromCallable {
            val application = getApplication<Application>()
            val f = StorageService(application).file(attachment.path)
            FileProvider.getUriForFile(application, "to.freebots.fileprovider", f)
        }.subscribe {
            uri.postValue(Event(it))
        })
    }
}
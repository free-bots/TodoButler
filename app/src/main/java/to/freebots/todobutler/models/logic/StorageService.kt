package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import androidx.core.net.toFile
import io.reactivex.Observable
import to.freebots.todobutler.common.logic.BaseLogicService
import to.freebots.todobutler.models.entities.Attachment
import java.io.File
import java.io.InputStream
import java.lang.Exception
import java.util.*

class StorageService(private val application: Application) {

    companion object {
        const val FILES_ROOT = "/tasks"
    }

    init {
        prepare()
    }

    fun saveFile(uri: Uri): String {

        val file = File(storage().path, UUID.randomUUID().toString())

        application.contentResolver.openInputStream(uri).use { inputStream: InputStream? ->
            inputStream?.readBytes()?.let {
                file.writeBytes(it)
            }
        }

        if (!file.exists()) {
            throw Exception("error")
        }

        // todo save file from uri
        return file.name
    }

    fun saveAllFiles(uris: MutableList<Uri>): String {
        // todo save file from uri
        return ""
    }

    fun makeCopyOfFile(uri: Uri): String {

        return ""
    }

    fun makeCopyOfAllFiles(uris: MutableList<Uri>): String {

        return ""
    }


    fun removeFile(fileName: String) {
        val file = File(storage(), fileName)

        if (!file.exists() || file.delete()) {
            throw Exception("error")
        }
    }

    private fun storage(): File {
        return File(application.filesDir, FILES_ROOT)
    }

    private fun prepare() {
        if (storage().exists().not()) {
            if (!storage().mkdir()) {
                throw Exception("can not create files dir")
            }
        }
    }

    fun nuke() {
        if (!storage().deleteRecursively()) {
            throw Exception("can not nuke files");
        }
    }

    fun fileName(uri: Uri): String = uri.lastPathSegment!!.substringAfterLast("/")
}
package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import to.freebots.todobutler.common.logic.BaseLogicService
import java.io.File
import java.lang.Exception

class StorageService(private val application: Application) {

    companion object{
        const val FILES_ROOT = "/tasks"
    }

    init {
        prepare()
    }

    fun saveFile(uri: Uri): String {
        // todo save file from uri
        return ""
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
}
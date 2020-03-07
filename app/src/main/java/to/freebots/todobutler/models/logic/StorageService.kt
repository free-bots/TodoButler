package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import to.freebots.todobutler.common.logic.BaseLogicService
import java.io.File

class StorageService(private val application: Application) {


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


    fun storage(): File {
        // todo navigate to the file storage
        return application.filesDir
    }
}
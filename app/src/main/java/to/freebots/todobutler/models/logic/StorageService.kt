package to.freebots.todobutler.models.logic

import android.app.Application
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.util.*

class StorageService(private val application: Application) {

    companion object {
        const val FILES_ROOT = "/tasks"
        const val SPACE_BUFFER = 1.10
    }

    init {
        prepare()
    }

    fun saveFile(uri: Uri): String {

        val file = file(UUID.randomUUID().toString())

        application.contentResolver.openInputStream(uri).use { inputStream: InputStream? ->
            inputStream?.readBytes()?.let {
                file.writeBytes(it)
            }
        }

        if (!file.exists()) {
            throw Exception("error")
        }

        return file.name
    }

    fun makeCopyOfAllFiles(paths: MutableList<String>): MutableList<String> {
        return paths.map { path: String ->
            val file = file(path)
            val copy = file(UUID.randomUUID().toString())

            if (!file.exists()) {
                throw Exception()
            }

            copy.writeBytes(file.readBytes())

            copy.name
        }.toMutableList()
    }


    fun removeFile(fileName: String) {
        val file = file(fileName)

        if (!file.exists() || !file.delete()) {
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
            throw Exception("can not nuke files")
        }
    }

    fun fileName(uri: Uri): String = uri.lastPathSegment!!.substringAfterLast("/")

    fun freeSpace(): Long = storage().freeSpace

    fun file(name: String) = File(storage(), name)
}
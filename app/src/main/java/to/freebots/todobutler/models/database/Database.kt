package to.freebots.todobutler.models.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import to.freebots.todobutler.models.dto.AttachmentDAO
import to.freebots.todobutler.models.dto.LabelDAO
import to.freebots.todobutler.models.dto.TaskDAO
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.entities.Label
import to.freebots.todobutler.models.entities.Task

@androidx.room.Database(
    entities = [Task::class, Label::class, Attachment::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun labelDAO(): LabelDAO
    abstract fun attachmentDAO(): AttachmentDAO
    abstract fun taskDAO(): TaskDAO

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.clearAllTables()
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun getDatabase(context: Context): Database {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Database::class.java,
                    "database"
                )
//                    .addCallback(DatabaseCallback(GlobalScope))
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
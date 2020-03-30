package to.freebots.todobutler.models.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import to.freebots.todobutler.common.mock.Mock
import to.freebots.todobutler.models.dto.*
import to.freebots.todobutler.models.entities.*

@androidx.room.Database(
    entities = [
        Task::class,
        Label::class,
        Attachment::class,
        Location::class,
        Reminder::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConverter::class)
abstract class Database : RoomDatabase() {

    abstract fun labelDAO(): LabelDAO
    abstract fun attachmentDAO(): AttachmentDAO
    abstract fun taskDAO(): TaskDAO
    abstract fun locationDAO(): LocationDAO
    abstract fun reminderDAO(): ReminderDAO

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    database.clearAllTables()
                    Mock.applyToDatabase(database)
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
                    .addCallback(DatabaseCallback(GlobalScope))
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}

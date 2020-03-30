package to.freebots.todobutler.models.database

import androidx.room.TypeConverter
import to.freebots.todobutler.models.entities.Priority
import java.util.*

class TypeConverter {

    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun priorityToString(priority: Priority): String? {
        return priority.name
    }

    @TypeConverter
    fun stringToPriority(priority: String): Priority? {
        return Priority.valueOf(priority)
    }
}

package to.freebots.todobutler.models.database

import androidx.room.TypeConverter
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
}

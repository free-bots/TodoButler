package to.freebots.todobutler.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Reminder(
    var date: Date,
    var span: Long?,
    id: Long?,
    createdAt: Date?,
    updatedAt: Date?
) :
    BaseEntity(id, createdAt, updatedAt), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Date::class.java.classLoader) as Date,
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Date::class.java.classLoader) as Date?,
        parcel.readValue(Date::class.java.classLoader) as Date?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(date)
        parcel.writeValue(span)
        parcel.writeValue(id)
        parcel.writeValue(createdAt)
        parcel.writeValue(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reminder> {
        override fun createFromParcel(parcel: Parcel): Reminder {
            return Reminder(parcel)
        }

        override fun newArray(size: Int): Array<Reminder?> {
            return arrayOfNulls(size)
        }
    }
}
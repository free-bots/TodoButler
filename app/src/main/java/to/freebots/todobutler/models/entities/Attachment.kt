package to.freebots.todobutler.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Attachment(
    var taskId: Long,
    var name: String,
    var path: String,
    var extension: String,
    id: Long? = null,
    createdAt: Date? = null,
    updatedAt: Date? = null
) :
    BaseEntity(id, createdAt, updatedAt), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Date::class.java.classLoader) as Date?,
        parcel.readValue(Date::class.java.classLoader) as Date?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(taskId)
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeString(extension)
        parcel.writeValue(id)
        parcel.writeValue(createdAt)
        parcel.writeValue(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attachment> {
        override fun createFromParcel(parcel: Parcel): Attachment {
            return Attachment(parcel)
        }

        override fun newArray(size: Int): Array<Attachment?> {
            return arrayOfNulls(size)
        }
    }
}

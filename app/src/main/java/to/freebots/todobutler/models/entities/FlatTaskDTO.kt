package to.freebots.todobutler.models.entities

import android.os.Parcel
import android.os.Parcelable
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

class FlatTaskDTO(
    var label: Label,
    var parentTaskId: Long?,
    var name: String,
    var description: String,
    var isCompleted: Boolean,
    var isPinned: Boolean,
    var color: String,
    var subTasks: MutableList<FlatTaskDTO>,
    var attachments: MutableList<Attachment>,
    id: Long? = 0,
    createdAt: Date? = null,
    updatedAt: Date? = null
) :
    BaseEntity(id, createdAt, updatedAt), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Label::class.java.classLoader)!!,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readParcelableArray(FlatTaskDTO::class.java.classLoader)!!.toMutableList<Parcelable>() as MutableList<FlatTaskDTO>,
        parcel.readParcelableArray(Attachment::class.java.classLoader)!!.toMutableList<Parcelable>() as MutableList<Attachment>,
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Date::class.java.classLoader) as Date?,
        parcel.readValue(Date::class.java.classLoader) as Date?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(label, flags)
        parcel.writeValue(parentTaskId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeByte(if (isCompleted) 1 else 0)
        parcel.writeByte(if (isPinned) 1 else 0)
        parcel.writeString(color)
        parcel.writeParcelableArray(subTasks.toTypedArray(), flags)
        parcel.writeParcelableArray(attachments.toTypedArray(), flags)
        parcel.writeValue(id)
        parcel.writeValue(createdAt)
        parcel.writeValue(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlatTaskDTO> {
        override fun createFromParcel(parcel: Parcel): FlatTaskDTO {
            return FlatTaskDTO(parcel)
        }

        override fun newArray(size: Int): Array<FlatTaskDTO?> {
            return arrayOfNulls(size)
        }
    }
}

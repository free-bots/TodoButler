package to.freebots.todobutler.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Location(
    var latitude: Double,
    var longitude: Double,
    //var text: String // text to display?
    id: Long? = null,
    createdAt: Date? = null,
    updatedAt: Date? = null
) : BaseEntity(id, createdAt, updatedAt), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readValue(Long::class.java.classLoader) as Long?,
        parcel.readValue(Date::class.java.classLoader) as Date?,
        parcel.readValue(Date::class.java.classLoader) as Date?
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeValue(id)
        parcel.writeValue(createdAt)
        parcel.writeValue(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Location> {
        override fun createFromParcel(parcel: Parcel): Location {
            return Location(parcel)
        }

        override fun newArray(size: Int): Array<Location?> {
            return arrayOfNulls(size)
        }
    }
}

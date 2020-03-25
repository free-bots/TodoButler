package to.freebots.todobutler.models.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Entity
class Label(
    var name: String,
    val icon: String,
    id: Long? = null,
    createdAt: Date? = null,
    updatedAt: Date? = null
) : BaseEntity(id, createdAt, updatedAt), Parcelable {

    constructor(parcel: Parcel) : this("","", 0) {
        val values: Array<Any>? = parcel.readArray(ClassLoader.getSystemClassLoader())

        this.id = values?.get(0) as Long? ?: 0
        this.name = values?.get(1) as String? ?: ""
        this.name = values?.get(2) as String? ?: ""
        this.createdAt = values?.get(3) as Date
        this.updatedAt = values[4] as Date
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(
            mutableListOf(
                id.toString(),
                name,
                icon,
                createdAt?.toString().toString(),
                updatedAt?.toString().toString()
            )
        )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Label> {
        override fun createFromParcel(parcel: Parcel): Label {
            return Label(parcel)
        }

        override fun newArray(size: Int): Array<Label?> {
            return arrayOfNulls(size)
        }
    }
}
// TODO: add icon

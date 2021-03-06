package to.freebots.todobutler.common.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
abstract class BaseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var createdAt: Date? = null,
    var updatedAt: Date? = null
)

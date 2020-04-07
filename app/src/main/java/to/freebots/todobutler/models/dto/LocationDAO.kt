package to.freebots.todobutler.models.dto

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import to.freebots.todobutler.common.dao.BaseDAO
import to.freebots.todobutler.models.entities.Location

@Dao
abstract class LocationDAO : BaseDAO<Location> {

    @Query("SELECT * FROM Location")
    abstract fun findAll(): MutableList<Location>

    @Query("SELECT * FROM Location WHERE id=:id")
    abstract fun findLocationById(id: Long): Location

    @Query("SELECT * FROM Location WHERE rowid=:rowId")
    abstract fun findByRowId(rowId: Long): Location

    @Transaction
    open fun createLocation(location: Location): Location {
        val rowId = create(location)
        return findByRowId(rowId)
    }

    @Transaction
    open fun updateLocation(location: Location): Location {
        update(location)
        return findLocationById(location.id!!)
    }
}
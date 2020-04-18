package to.freebots.todobutler.common.dao

import androidx.room.*
import to.freebots.todobutler.common.entities.BaseEntity
import java.util.*

@Dao
abstract class BaseDAO<E : BaseEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun createInDB(e: E): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updateInDB(e: E): Int

    @Delete
    abstract fun delete(e: E): Int

    @Transaction
    open fun create(e: E): Long {
        return createInDB(e.apply { createdAt = Date() })
    }

    @Transaction
    open fun update(e: E): Int {
        return updateInDB(e.apply { updatedAt = Date() })
    }
}

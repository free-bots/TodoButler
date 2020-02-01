package to.freebots.todobutler.common.dao

import androidx.room.*
import to.freebots.todobutler.common.entities.BaseEntity

@Dao
interface BaseDAO<E: BaseEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(e: E): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(e: E): Int

    @Delete
    fun delete(e: E): Int
}
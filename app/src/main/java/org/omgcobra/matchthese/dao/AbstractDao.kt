package org.omgcobra.matchthese.dao

import androidx.room.*
import org.omgcobra.matchthese.model.AbstractEntity

interface AbstractDao<T: AbstractEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg items: T): List<Long>

    fun load(id: Long): T

    @Update
    fun update(vararg items: T)

    @Delete
    fun delete(vararg items: T)

    fun deleteAll()
}
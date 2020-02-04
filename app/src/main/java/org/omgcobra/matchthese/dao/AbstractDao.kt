package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.NamedEntity

interface AbstractDao<T: AbstractEntity<T>> {

    fun getAll(): LiveData<List<T>>

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

interface NamedDao<T: NamedEntity<T>>: AbstractDao<T> {
    fun getByName(name: String): T?
}
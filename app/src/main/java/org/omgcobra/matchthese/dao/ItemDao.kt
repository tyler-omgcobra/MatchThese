package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.Item

@Dao
interface ItemDao: AbstractDao<Item> {

    @Query("SELECT * FROM Item")
    override fun getAll(): LiveData<List<Item>>

    @Query("SELECT * FROM Item WHERE id = :id")
    override fun load(id: Long): Item

    @Query("DELETE FROM Item")
    override fun deleteAll()

    @Query("SELECT * FROM Item WHERE name = :name")
    fun findItemsByName(name: String): List<Item>
}

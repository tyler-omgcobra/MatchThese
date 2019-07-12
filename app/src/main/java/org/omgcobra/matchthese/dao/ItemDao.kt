package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItems(vararg items: Item)

    @Update
    fun updateItems(vararg items: Item)

    @Delete
    fun deleteItems(vararg items: Item)

    @Query("SELECT * FROM Item")
    fun allItems(): LiveData<List<Item>>

    @Query("SELECT * FROM Item WHERE id = :id")
    fun loadItem(id: Int): Item

    @Query("""
        SELECT *
        FROM Item
            INNER JOIN ItemTagJoin
            INNER JOIN Tag
        GROUP BY Item.id
    """)
    fun loadItemsWithTags(): LiveData<List<ItemWithTags>>
}

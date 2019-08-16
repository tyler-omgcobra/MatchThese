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
        SELECT Item.id, Item.name, Tag.id tagid, Tag.name tagname
        FROM Item
            LEFT OUTER JOIN ItemTagJoin
            LEFT OUTER JOIN Tag
        GROUP BY Item.id
    """)
    fun allItemsWithTags(): LiveData<List<ItemWithTags>>

    @Query("DELETE FROM Item")
    fun deleteAll()
}

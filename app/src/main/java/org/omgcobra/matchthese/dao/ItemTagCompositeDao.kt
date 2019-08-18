package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.ItemWithTags
import org.omgcobra.matchthese.model.TagWithItems

@Dao
interface ItemTagCompositeDao {
    @Transaction
    @Query("SELECT * FROM Item")
    fun allItemsWithTags(): LiveData<List<ItemWithTags>>

    @Transaction
    @Query("SELECT * FROM Item WHERE Item.id = :itemId")
    fun loadItemWithTags(itemId: Long): ItemWithTags

    @Transaction
    @Query("SELECT * FROM Tag")
    fun allTagsWithItems(): LiveData<List<TagWithItems>>

    @Transaction
    @Query("SELECT * FROM Tag WHERE Tag.id = :tagId")
    fun loadTagWithItems(tagId: Long): TagWithItems
}

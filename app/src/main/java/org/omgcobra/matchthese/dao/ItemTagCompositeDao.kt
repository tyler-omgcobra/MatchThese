package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags

@Dao
interface ItemTagCompositeDao {
    @Query("""
        SELECT Item.id, Item.name, Tag.id tagid, Tag.name tagname
        FROM Item
            LEFT OUTER JOIN ItemTagJoin
            LEFT OUTER JOIN Tag
        GROUP BY Item.id
    """)
    fun allItemsWithTags(): LiveData<List<ItemWithTags>>

    @Query("""
        SELECT Item.id, Item.name, Tag.name tagList
        FROM Item
            LEFT OUTER JOIN ItemTagJoin
            LEFT OUTER JOIN Tag
        WHERE Item.id = :itemId
        GROUP BY Item.id
    """)
    fun loadItemWithTags(itemId: Long): ItemWithTags
}

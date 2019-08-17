package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.Tag

@Dao
interface TagDao: AbstractDao<Tag> {
    @Query("SELECT * FROM Tag")
    fun allTags(): LiveData<List<Tag>>

    @Query("SELECT * FROM Tag WHERE id = :id")
    override fun load(id: Long): Tag

    @Query("SELECT * FROM Tag WHERE name = :name")
    fun findTagsByName(name: String): List<Tag>

    @Query("DELETE FROM Tag")
    override fun deleteAll()
}

package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.omgcobra.matchthese.model.ItemTagJoin

@Dao
interface ItemTagJoinDao: AbstractDao<ItemTagJoin> {

    @Query("SELECT * FROM ItemTagJoin")
    override fun getAll(): LiveData<List<ItemTagJoin>>

    @Query("SELECT * FROM ItemTagJoin WHERE id = :id")
    override fun load(id: Long): ItemTagJoin

    @Query("DELETE FROM ItemTagJoin")
    override fun deleteAll()
}
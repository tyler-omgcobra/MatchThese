package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.Ingredient

@Dao
interface IngredientDao: AbstractDao<Ingredient> {
    @Query("SELECT * FROM Ingredient")
    override fun getAll(): LiveData<List<Ingredient>>

    @Query("SELECT * FROM Ingredient WHERE id = :id")
    override fun load(id: Long): Ingredient

    @Query("DELETE FROM Ingredient")
    override fun deleteAll()

    @Query("SELECT * FROM Ingredient WHERE name = :name")
    fun getByName(name: String): Ingredient?
}

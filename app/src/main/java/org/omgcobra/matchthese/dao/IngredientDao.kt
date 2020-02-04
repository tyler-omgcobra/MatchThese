package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.omgcobra.matchthese.model.Ingredient

@Dao
interface IngredientDao: NamedDao<Ingredient> {
    @Query("SELECT * FROM Ingredient")
    override fun getAll(): LiveData<List<Ingredient>>

    @Query("SELECT * FROM Ingredient WHERE id = :id")
    override fun load(id: Long): Ingredient

    @Query("DELETE FROM Ingredient")
    override fun deleteAll()

    @Query("SELECT * FROM Ingredient WHERE name = :name")
    override fun getByName(name: String): Ingredient?

    @Query("SELECT * FROM Ingredient WHERE inPantry = 1")
    fun getPantry(): LiveData<List<Ingredient>>
}

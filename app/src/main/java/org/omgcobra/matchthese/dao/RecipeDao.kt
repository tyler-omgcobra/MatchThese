package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.omgcobra.matchthese.model.Recipe

@Dao
interface RecipeDao: NamedDao<Recipe> {

    @Query("SELECT * FROM Recipe")
    override fun getAll(): LiveData<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    override fun load(id: Long): Recipe

    @Query("DELETE FROM Recipe")
    override fun deleteAll()

    @Query("SELECT * FROM Recipe WHERE name = :name")
    override fun getByName(name: String): Recipe?

    @Query("SELECT * FROM Recipe WHERE onShoppingList = 1")
    fun getShoppingList(): LiveData<List<Recipe>>
}

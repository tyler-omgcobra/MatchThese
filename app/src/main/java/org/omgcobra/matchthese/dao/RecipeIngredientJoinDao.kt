package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.omgcobra.matchthese.model.RecipeIngredientJoin

@Dao
interface RecipeIngredientJoinDao: AbstractDao<RecipeIngredientJoin> {

    @Query("SELECT * FROM RecipeIngredientJoin")
    override fun getAll(): LiveData<List<RecipeIngredientJoin>>

    @Query("SELECT * FROM RecipeIngredientJoin WHERE id = :id")
    override fun load(id: Long): RecipeIngredientJoin

    @Query("DELETE FROM RecipeIngredientJoin")
    override fun deleteAll()

    @Query("SELECT * FROM RecipeIngredientJoin WHERE recipeid = :recipeId AND ingredientid = :ingredientId")
    fun getByRecipeAndIngredient(recipeId: Long, ingredientId: Long): RecipeIngredientJoin?
}
package org.omgcobra.matchthese.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import org.omgcobra.matchthese.model.RecipeWithIngredients
import org.omgcobra.matchthese.model.IngredientWithRecipes

@Dao
interface RecipeIngredientCompositeDao {
    @Transaction
    @Query("SELECT * FROM Recipe")
    fun allRecipesWithIngredients(): LiveData<List<RecipeWithIngredients>>

    @Transaction
    @Query("SELECT * FROM Recipe WHERE Recipe.id = :recipeId")
    fun loadRecipeWithIngredients(recipeId: Long): RecipeWithIngredients

    @Transaction
    @Query("SELECT * FROM Ingredient")
    fun allIngredientsWithRecipes(): LiveData<List<IngredientWithRecipes>>

    @Transaction
    @Query("SELECT * FROM Ingredient WHERE Ingredient.id = :ingredientId")
    fun loadIngredientWithRecipes(ingredientId: Long): IngredientWithRecipes
}

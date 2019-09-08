package org.omgcobra.matchthese.fragments.ingredient

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.model.Ingredient

class IngredientWithRecipesViewModel(application: Application): AndroidViewModel(application) {
    private val ingredientsWithRecipesLiveData = AppRepository.getIngredientsWithRecipes()

    fun getIngredientsWithRecipesList() = ingredientsWithRecipesLiveData

    fun insert(ingredient: Ingredient) = AppRepository.insert(ingredient)
    fun delete(ingredient: Ingredient) = AppRepository.delete(ingredient)
}
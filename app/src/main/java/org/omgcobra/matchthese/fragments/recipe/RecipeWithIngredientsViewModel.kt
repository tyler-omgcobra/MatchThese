package org.omgcobra.matchthese.fragments.recipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.model.Recipe

class RecipeWithIngredientsViewModel(application: Application): AndroidViewModel(application) {
    private val recipesWithIngredientsLiveData = AppRepository.getRecipesWithIngredients()

    fun getRecipesWithIngredientsList() = recipesWithIngredientsLiveData

    fun insert(recipe: Recipe) = AppRepository.insert(recipe)
    fun delete(recipe: Recipe) = AppRepository.delete(recipe)
}
package org.omgcobra.matchthese.fragments.ingredient

import android.widget.ArrayAdapter
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.data.RecipeRowEditAdapter
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.IngredientWithRecipes
import org.omgcobra.matchthese.model.Recipe

class IngredientEditFragment: CompositeListEntityEditFragment<Ingredient, Recipe>() {
    override val hintId = R.string.recipes
    override val liveData = AppRepository.getAll<Recipe>()

    override fun createRowEditAdapter(entityAdapter: ArrayAdapter<Recipe>) = RecipeRowEditAdapter(requireContext(), entityAdapter)

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val ingredient = listEntity?.entity ?: Ingredient(name)
        ingredient.name = name

        if (listEntity != null) {
            AppRepository.update(ingredient)
        } else {
            listEntity = IngredientWithRecipes(ingredient)
            AppRepository.insert(ingredient)
        }

        listEntity!!.joinList.filter { join -> !rowEditAdapter.dataSet.any { it.recipe.name == join.recipe.name } }
                .forEach { AppRepository.removeRecipeFromIngredient(listEntity!!, it.recipe.name) }
        rowEditAdapter.dataSet.forEach { AppRepository.ensureRecipeHasIngredient(listEntity!!, it.recipe.name, it.amount)}

        super.saveItem()
    }
}
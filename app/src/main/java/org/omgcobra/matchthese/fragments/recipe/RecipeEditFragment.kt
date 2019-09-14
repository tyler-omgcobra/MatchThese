package org.omgcobra.matchthese.fragments.recipe

import android.widget.ArrayAdapter
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.data.IngredientRowEditAdapter
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeWithIngredients

class RecipeEditFragment: CompositeListEntityEditFragment<Recipe, Ingredient>() {
    override val hintId = R.string.ingredient
    override val liveData = AppRepository.getAll<Ingredient>()

    override fun createRowEditAdapter(entityAdapter: ArrayAdapter<Ingredient>) = IngredientRowEditAdapter(requireContext(), entityAdapter)

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val recipe = listEntity?.entity ?: Recipe(name)
        recipe.name = name

        if (listEntity != null) {
            AppRepository.update(recipe)
        } else {
            listEntity = RecipeWithIngredients(recipe)
            AppRepository.insert(recipe)
        }

        listEntity!!.joinList.filter { join -> !rowEditAdapter.dataSet.any { it.ingredient!!.name == join.ingredient!!.name } }
                .forEach { AppRepository.removeIngredientFromRecipe(listEntity!!, it.ingredient!!.name)}
        rowEditAdapter.dataSet.forEach { AppRepository.ensureIngredientInRecipe(listEntity!!, it.ingredient!!.name, it.amount) }

        super.saveItem()
    }
}
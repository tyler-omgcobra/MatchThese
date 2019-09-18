package org.omgcobra.matchthese.fragments.recipe

import android.widget.ArrayAdapter
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.data.IngredientRowEditAdapter
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeIngredientJoin
import org.omgcobra.matchthese.model.RecipeWithIngredients

class RecipeEditFragment: CompositeListEntityEditFragment<Recipe, Ingredient>() {
    override val hintId = R.string.ingredient
    override val liveData = AppRepository.getAll<Ingredient>()

    override fun createRowEditAdapter(entityAdapter: ArrayAdapter<Ingredient>) = IngredientRowEditAdapter(requireContext(), entityAdapter)
    override fun getEntity(name: String) = listEntity?.entity ?: Recipe(name)
    override fun makeListEntity(entity: Recipe) = RecipeWithIngredients(entity)
    override fun getListEntityName(join: RecipeIngredientJoin) = join.ingredient!!.name

    override fun insertEntity(entity: Recipe) {
        AppRepository.insert(entity)
    }

    override fun updateEntity(entity: Recipe) {
        AppRepository.update(entity)
    }

    override fun addToListEntity(name: String, amount: String) {
        AppRepository.ensureIngredientInRecipe(listEntity!!, name, amount)
    }

    override fun removeFromListEntity(name: String) {
        AppRepository.removeIngredientFromRecipe(listEntity!!, name)
    }
}
package org.omgcobra.matchthese.fragments.ingredient

import android.widget.ArrayAdapter
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.data.RecipeRowEditAdapter
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.IngredientWithRecipes
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeIngredientJoin
import java.math.BigDecimal

class IngredientEditFragment: CompositeListEntityEditFragment<Ingredient, Recipe>() {
    override val hintId = R.string.recipes
    override val liveData = AppRepository.getAll<Recipe>()

    override fun createRowEditAdapter(entityAdapter: ArrayAdapter<Recipe>) = RecipeRowEditAdapter(requireContext(), entityAdapter)
    override fun getEntity(name: String) = listEntity?.entity ?: Ingredient(name)
    override fun makeListEntity(entity: Ingredient) = IngredientWithRecipes(entity)
    override fun getListEntityName(join: RecipeIngredientJoin) = join.recipe.name

    override fun insertEntity(entity: Ingredient) {
        AppRepository.insert(entity)
    }

    override fun updateEntity(entity: Ingredient) {
        AppRepository.update(entity)
    }

    override fun addToListEntity(name: String, amount: BigDecimal, unit: String) {
        AppRepository.ensureRecipeHasIngredient(listEntity!!, name, amount, unit)
    }

    override fun removeFromListEntity(name: String) {
        AppRepository.removeRecipeFromIngredient(listEntity!!, name)
    }

}
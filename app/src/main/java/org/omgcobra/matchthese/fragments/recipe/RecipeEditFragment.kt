package org.omgcobra.matchthese.fragments.recipe

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeWithIngredients
import org.omgcobra.matchthese.model.Ingredient

class RecipeEditFragment: CompositeListEntityEditFragment<Recipe, Ingredient>() {
    override val hintId = R.string.ingredients
    override val layoutId = R.layout.fragment_edit_recipe

    override fun initEntity(view: View) {
        val adapter = createAdapter()
        setupListEditText(adapter)
        listEditText.setText("%s,".format(listEntity?.joinList?.joinToString { it.ingredient.toString() }))
        listEntity?.joinList?.forEach {
            listEditText.addObjectSync(it.ingredient)
        }
        AppRepository.getAll<Ingredient>().observe(this) { ingredientList ->
            adapter.clear()
            adapter.addAll(ingredientList)
        }
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val ingredients = listEditText.objects
        val recipe = listEntity?.entity ?: Recipe(name)
        recipe.name = name

        if (listEntity != null) {
            AppRepository.update(recipe)
        } else {
            listEntity = RecipeWithIngredients(recipe)
            AppRepository.insert(recipe)
        }

        listEntity!!.joinList.filter { !ingredients.contains(it.ingredient) }
                .forEach { AppRepository.removeIngredientFromRecipe(listEntity!!, it.ingredient!!.name) }

        ingredients.forEach { AppRepository.ensureIngredientInRecipe(listEntity!!, it.name) }

        super.saveItem()
    }
}
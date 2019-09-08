package org.omgcobra.matchthese.fragments.ingredient

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.IngredientWithRecipes

class IngredientEditFragment: CompositeListEntityEditFragment<Ingredient, Recipe>() {
    override val hintId = R.string.recipes
    override val layoutId = R.layout.fragment_edit_ingredient

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val adapter = createAdapter()
        setupListEditText(adapter)
        listEditText.setText("%s,".format(listEntity?.joinList?.joinToString { it.recipe.toString() }))
        listEntity?.joinList?.forEach {
            listEditText.addObjectSync(it.recipe)
        }
        AppRepository.getAll<Recipe>().observe(this) { itemList ->
            adapter.clear()
            adapter.addAll(itemList)
        }
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val items = listEditText.objects
        val ingredient = listEntity?.entity ?: Ingredient(name)
        ingredient.name = name

        if (listEntity != null) {
            AppRepository.update(ingredient)
        } else {
            listEntity = IngredientWithRecipes(ingredient)
            AppRepository.insert(ingredient)
        }

        listEntity!!.joinList.filter { !items.contains(it.recipe) }
                .forEach { AppRepository.removeRecipeFromIngredient(listEntity!!, it.recipe.name) }

        items.forEach { AppRepository.ensureRecipeHasIngredient(listEntity!!, it.name) }

        super.saveItem()
    }
}
package org.omgcobra.matchthese.fragments.ingredient

import android.content.Context
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.Ingredient

class IngredientWithRecipesAdapter(context: Context) : CompositeListEntityAdapter<Ingredient, Recipe>(context) {
    override val editActionId = R.id.edit_ingredient

    override fun getSubText(listEntity: CompositeNamedListEntity<Ingredient, *>) = listEntity.joinList.joinToString { it.recipe.name }

    override fun delete(position: Int) {
        super.delete(position)

        AppRepository.delete(deletedItem!!.entity)
    }
}
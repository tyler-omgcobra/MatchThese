package org.omgcobra.matchthese.fragments.recipe

import android.content.Context
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.Ingredient

class RecipeWithIngredientsAdapter(context: Context) : CompositeListEntityAdapter<Recipe, Ingredient>(context) {
    override val editActionId = R.id.edit_recipe

    override fun getSubText(listEntity: CompositeNamedListEntity<Recipe, *>) = listEntity.joinList.joinToString { it.ingredient!!.toString() }

    override fun delete(position: Int) {
        super.delete(position)

        AppRepository.delete(deletedItem!!.entity)
    }
}
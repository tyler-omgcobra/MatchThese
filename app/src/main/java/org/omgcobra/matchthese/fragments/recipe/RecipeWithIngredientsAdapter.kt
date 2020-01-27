package org.omgcobra.matchthese.fragments.recipe

import android.content.Context
import android.graphics.Color
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.Recipe

class RecipeWithIngredientsAdapter(context: Context) : CompositeListEntityAdapter<Recipe, Ingredient>(context) {
    override val editActionId = R.id.edit_recipe

    override fun getSubText(listEntity: CompositeNamedListEntity<Recipe, *>) = listEntity.joinList.joinToString { it.ingredient!!.toString() }
    override fun getImage(listEntity: CompositeNamedListEntity<Recipe, *>) = R.drawable.ic_shopping_cart_black_24dp
    override fun getImageTint(listEntity: CompositeNamedListEntity<Recipe, *>): Int {
        return when {
            listEntity.entity.onShoppingList -> Color.DKGRAY
            else -> Color.LTGRAY
        }
    }

    override fun handleImageClick(listEntity: CompositeNamedListEntity<Recipe, *>) {
        listEntity.entity.onShoppingList = !listEntity.entity.onShoppingList
        AppRepository.update(listEntity.entity)
    }

    override fun delete(position: Int) {
        super.delete(position)

        AppRepository.delete(deletedItem!!.entity)
    }
}
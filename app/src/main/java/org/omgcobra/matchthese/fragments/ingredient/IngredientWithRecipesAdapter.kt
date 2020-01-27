package org.omgcobra.matchthese.fragments.ingredient

import android.content.Context
import android.graphics.Color
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.Recipe

class IngredientWithRecipesAdapter(context: Context) : CompositeListEntityAdapter<Ingredient, Recipe>(context) {
    override val editActionId = R.id.edit_ingredient

    override fun getSubText(listEntity: CompositeNamedListEntity<Ingredient, *>) = listEntity.joinList.joinToString { it.recipe.name }
    override fun getImage(listEntity: CompositeNamedListEntity<Ingredient, *>) = R.drawable.ic_kitchen_black_24dp
    override fun getImageTint(listEntity: CompositeNamedListEntity<Ingredient, *>): Int {
        return when {
            listEntity.entity.inPantry -> Color.DKGRAY
            else -> Color.LTGRAY
        }
    }

    override fun handleImageClick(listEntity: CompositeNamedListEntity<Ingredient, *>) {
        listEntity.entity.inPantry = !listEntity.entity.inPantry
        AppRepository.update(listEntity.entity)
    }

    override fun delete(position: Int) {
        super.delete(position)

        AppRepository.delete(deletedItem!!.entity)
    }
}
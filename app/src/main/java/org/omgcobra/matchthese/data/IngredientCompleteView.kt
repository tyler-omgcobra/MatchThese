package org.omgcobra.matchthese.data

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokenautocomplete.TokenCompleteTextView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.NamedEntity
import org.omgcobra.matchthese.model.Ingredient

fun getView(entity: NamedEntity<*>, context: Context, viewGroup: ViewGroup): View {
    val l = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = l.inflate(R.layout.entity_token, viewGroup, false) as TextView
    view.text = entity.toString()

    return view

}

class IngredientCompleteView(context: Context, attrs: AttributeSet): TokenCompleteTextView<Ingredient>(context, attrs) {
    override fun getViewForObject(ingredient: Ingredient) = getView(ingredient, context, parent as ViewGroup)
    override fun defaultObject(completionText: String) = Ingredient(completionText)
}

class RecipeCompleteView(context: Context, attrs: AttributeSet): TokenCompleteTextView<Recipe>(context, attrs) {
    override fun getViewForObject(recipe: Recipe) = getView(recipe, context, parent as ViewGroup)
    override fun defaultObject(completionText: String) = Recipe(completionText)
}

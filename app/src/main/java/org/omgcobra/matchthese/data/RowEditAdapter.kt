package org.omgcobra.matchthese.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.fragments.Swipable
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.NamedEntity
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeIngredientJoin

abstract class RowEditAdapter<L: NamedEntity<L>>(internal val context: Context, private val entityAdapter: ArrayAdapter<L>) : RecyclerView.Adapter<RowEditAdapter.RowViewHolder>(), Swipable {

    abstract val hintId: Int
    var dataSet: ArrayList<RecipeIngredientJoin> = ArrayList()
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> RowAddViewHolder(inflater.inflate(R.layout.add_row, parent, false))
        else -> RowEditViewHolder(inflater.inflate(R.layout.edit_row, parent, false))
    }

    override fun getItemCount() = dataSet.size + 1
    override fun getItemViewType(position: Int) = when (position) {
        dataSet.size -> 0
        else -> 1
    }

    protected abstract fun bindAutoComplete(holder: RowEditViewHolder, listEntity: RecipeIngredientJoin)
    open fun delete(position: Int) {
        AppRepository.delete(dataSet.removeAt(position))
    }

    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        when (holder) {
            is RowEditViewHolder -> {
                val listEntity = dataSet[position]
                holder.amountEditText.setText(listEntity.amount)
                holder.amountEditText.doAfterTextChanged { listEntity.amount = it.toString() }

                bindAutoComplete(holder, listEntity)
                holder.autoCompleteView.setAdapter(entityAdapter)
                holder.autoCompleteView.threshold = 1
                holder.autoCompleteView.setHint(hintId)
            }
            is RowAddViewHolder -> {
                holder.addRow.setOnClickListener{
                    dataSet.add(RecipeIngredientJoin(Recipe(""), Ingredient(""), ""))
                    notifyItemInserted(position)
                }
            }
        }
    }

    override fun onItemSwipe(position: Int) {
        if (position < dataSet.size) {
            delete(position)
            notifyItemRemoved(position)
        }
    }

    abstract class RowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class RowEditViewHolder(itemView: View): RowViewHolder(itemView) {
        val autoCompleteView: AutoCompleteTextView = itemView.findViewById(R.id.auto_complete_text)
        val amountEditText: EditText = itemView.findViewById(R.id.text_view)
    }

    class RowAddViewHolder(itemView: View): RowViewHolder(itemView) {
        val addRow: TextView = itemView.findViewById(R.id.add_row)
    }
}

class RecipeRowEditAdapter(context: Context, entityAdapter: ArrayAdapter<Recipe>): RowEditAdapter<Recipe>(context, entityAdapter) {
    override val hintId = R.string.recipe

    override fun bindAutoComplete(holder: RowEditViewHolder, listEntity: RecipeIngredientJoin) {
        holder.autoCompleteView.setText(listEntity.recipe.name)
        holder.autoCompleteView.doAfterTextChanged { listEntity.recipe = Recipe(it.toString()) }
    }
}

class IngredientRowEditAdapter(context: Context, entityAdapter: ArrayAdapter<Ingredient>): RowEditAdapter<Ingredient>(context, entityAdapter) {
    override val hintId = R.string.ingredient

    override fun bindAutoComplete(holder: RowEditViewHolder, listEntity: RecipeIngredientJoin) {
        holder.autoCompleteView.setText(listEntity.ingredient?.name)
        holder.autoCompleteView.doAfterTextChanged { listEntity.ingredient = Ingredient(it.toString()) }
    }
}
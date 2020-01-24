package org.omgcobra.matchthese.data

import android.content.Context
import android.icu.util.MeasureUnit
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.AppRepository
import org.omgcobra.matchthese.fragments.Swipable
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.NamedEntity
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.RecipeIngredientJoin
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

abstract class RowEditAdapter<L: NamedEntity<L>>(internal val context: Context, private val entityAdapter: ArrayAdapter<L>) : RecyclerView.Adapter<RowEditAdapter.RowViewHolder>(), Swipable {

    abstract val hintId: Int
    var dataSet: ArrayList<RecipeIngredientJoin> = ArrayList()
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val volumes = MeasureUnit.getAvailable("volume").map { it.subtype }
    private val weights = MeasureUnit.getAvailable("mass").map { it.subtype }
    private val unitAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, volumes + weights)
    private val df = DecimalFormat("#,###.#####")

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
                holder.unitAutoComplete.setAdapter(unitAdapter)
                holder.unitAutoComplete.threshold = 1

                val listEntity = dataSet[position]
                holder.amountEditText.setText(df.format(listEntity.amount))
                holder.unitAutoComplete.setText(listEntity.unit)
                val action: (Editable?) -> Unit = {
                    val numText = holder.amountEditText.text.toString()
                    listEntity.amount = if (numText.isNotEmpty()) BigDecimal(numText).setScale(5, RoundingMode.HALF_UP) else BigDecimal.ONE
                    listEntity.unit = holder.unitAutoComplete.text.toString()
                }
                holder.amountEditText.doAfterTextChanged(action)
                holder.unitAutoComplete.doAfterTextChanged(action)

                bindAutoComplete(holder, listEntity)
                holder.autoCompleteView.setAdapter(entityAdapter)
                holder.autoCompleteView.threshold = 1
                holder.autoCompleteView.setHint(hintId)
            }
            is RowAddViewHolder -> {
                holder.addRow.setOnClickListener{
                    dataSet.add(RecipeIngredientJoin(Recipe(""), Ingredient(""), BigDecimal.ZERO, ""))
                    notifyItemInserted(holder.adapterPosition)
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

    override fun createSwipeFlags(position: Int): Int {
        return when (position) {
            dataSet.size -> 0
            else -> ItemTouchHelper.START or ItemTouchHelper.END
        }
    }

    abstract class RowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    class RowEditViewHolder(itemView: View): RowViewHolder(itemView) {
        val autoCompleteView: AutoCompleteTextView = itemView.findViewById(R.id.auto_complete_text)
        val amountEditText: EditText = itemView.findViewById(R.id.number_view)
        val unitAutoComplete: AutoCompleteTextView = itemView.findViewById(R.id.unit_auto_complete)
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
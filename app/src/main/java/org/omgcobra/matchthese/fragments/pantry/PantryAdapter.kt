package org.omgcobra.matchthese.fragments.pantry

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.data.AbstractListEntityAdapter
import org.omgcobra.matchthese.data.CompositeListEntityViewHolder
import org.omgcobra.matchthese.model.Ingredient

class PantryAdapter(internal val context: Context) : AbstractListEntityAdapter<Ingredient, CompositeListEntityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositeListEntityViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return CompositeListEntityViewHolder(inflater.inflate(R.layout.draggable_main_sub_content_view, parent, false))
    }

    override fun onBindViewHolder(holder: CompositeListEntityViewHolder, position: Int) {
        val listEntity = dataSet[position]
        holder.mainText.text = listEntity.toString()
        holder.subText.text = "${listEntity.pantryAmount} ${listEntity.pantryUOM}"
    }
}
package org.omgcobra.matchthese.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.CompositeListEntity
import java.util.Collections

abstract class CompositeListEntityAdapter<E: AbstractEntity<E>>(internal val context: Context) : RecyclerView.Adapter<CompositeListEntityViewHolder>() {

    var dataSet: List<CompositeListEntity<E>> = emptyList()
        set(list) {
            field = list.sorted()
            notifyDataSetChanged()
        }
    protected var deletedItem: CompositeListEntity<E>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositeListEntityViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return CompositeListEntityViewHolder(inflater.inflate(R.layout.draggable_main_sub_content_view, parent, false))
    }

    override fun onBindViewHolder(holder: CompositeListEntityViewHolder, position: Int) {
        val itemWithTags = dataSet[position]
        holder.mainText.text = itemWithTags.entity.toString()
        holder.subText.text = itemWithTags.list.joinToString { it }
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(R.id.edit_item, bundleOf("itemWithTags" to itemWithTags))
        }
    }

    override fun getItemCount(): Int = dataSet.size

    open fun delete(position: Int) {
        deletedItem = dataSet[position]
    }

    fun onItemSwipe(position: Int) {
        delete(position)
        notifyItemRemoved(position)
    }

    fun onItemMove(from: Int, to: Int): Boolean {
        if (from < to) {
            for (i in from..to) {
                Collections.swap(dataSet, i, i + 1)
            }
        } else {
            for (i in from..to) {
                Collections.swap(dataSet, i, i - 1)
            }
        }
        notifyItemMoved(from, to)
        return true
    }
}

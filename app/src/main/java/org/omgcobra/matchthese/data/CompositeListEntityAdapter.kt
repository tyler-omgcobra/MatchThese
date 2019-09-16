package org.omgcobra.matchthese.data

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.fragments.Swipable
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.NamedEntity

abstract class CompositeListEntityAdapter<E: NamedEntity<E>, L: NamedEntity<L>>(internal val context: Context) : RecyclerView.Adapter<CompositeListEntityViewHolder>(), Swipable {

    var dataSet: ArrayList<CompositeNamedListEntity<E, L>> = ArrayList()
        set(list) {
            field = ArrayList(list.sorted())
            notifyDataSetChanged()
        }
    protected var deletedItem: CompositeNamedListEntity<E, *>? = null
    protected abstract val editActionId: Int

    abstract fun getSubText(listEntity: CompositeNamedListEntity<E, *>): String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompositeListEntityViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return CompositeListEntityViewHolder(inflater.inflate(R.layout.draggable_main_sub_content_view, parent, false))
    }

    override fun onBindViewHolder(holder: CompositeListEntityViewHolder, position: Int) {
        val listEntity = dataSet[position]
        holder.mainText.text = listEntity.toString()
        holder.subText.text = getSubText(listEntity)
        holder.itemView.setOnClickListener {
            it.findNavController().navigate(editActionId, bundleOf("listEntity" to listEntity))
        }
    }

    override fun getItemCount(): Int = dataSet.size

    open fun delete(position: Int) {
        deletedItem = dataSet.removeAt(position)
    }

    override fun onItemSwipe(position: Int) {
        delete(position)
        notifyItemRemoved(position)
    }

    override fun createSwipeFlags(position: Int): Int {
        return ItemTouchHelper.START or ItemTouchHelper.END
    }
}

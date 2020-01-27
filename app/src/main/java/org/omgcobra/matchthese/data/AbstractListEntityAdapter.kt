package org.omgcobra.matchthese.data

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.fragments.Swipable

abstract class AbstractListEntityAdapter<E: Comparable<E>, VH: RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>(), Swipable {
    var dataSet: ArrayList<E> = ArrayList()
        set(list) {
            field = ArrayList(list.sorted())
            notifyDataSetChanged()
        }
    protected var deletedItem: E? = null

    override fun getItemCount() = dataSet.size

    open fun delete(position: Int) {
        deletedItem = dataSet.removeAt(position)
    }

    override fun onItemSwipe(position: Int) {
        delete(position)
        notifyItemRemoved(position)
    }

    override fun createSwipeFlags(position: Int) = ItemTouchHelper.START or ItemTouchHelper.END
}
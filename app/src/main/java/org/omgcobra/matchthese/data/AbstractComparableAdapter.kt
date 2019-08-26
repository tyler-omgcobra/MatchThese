package org.omgcobra.matchthese.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

abstract class AbstractComparableAdapter<T: Comparable<T>, VH: RecyclerView.ViewHolder>(internal val context: Context) : RecyclerView.Adapter<VH>() {

    var dataSet: List<T> = emptyList()
        set(list) {
            field = list.sorted()
            notifyDataSetChanged()
        }
    protected var deletedItem: T? = null

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

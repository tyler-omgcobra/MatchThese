package org.omgcobra.matchthese.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractComparableAdapter<T: Comparable<T>, VH: RecyclerView.ViewHolder>(internal val context: Context) : RecyclerView.Adapter<VH>() {

    var dataSet: List<T> = emptyList()
        set(list) {
            field = list.sorted()
            notifyDataSetChanged()
        }
    protected var deletedItem: T? = null
    protected var deletedItemPosition: Int? = null

    override fun getItemCount(): Int = dataSet.size

    open fun delete(position: Int) {
        deletedItem = dataSet[position]
        deletedItemPosition = position
    }
}

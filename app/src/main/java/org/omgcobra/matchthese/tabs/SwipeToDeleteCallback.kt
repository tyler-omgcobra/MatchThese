package org.omgcobra.matchthese.tabs

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.data.AbstractComparableAdapter

class SwipeToDeleteCallback(private val adapter: AbstractComparableAdapter<*, *>): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.delete(viewHolder.adapterPosition)
    }
}

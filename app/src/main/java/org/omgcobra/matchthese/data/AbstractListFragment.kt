package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.NamedEntity

abstract class AbstractListFragment<E: NamedEntity<E>, A: CompositeListEntityAdapter<E>>: Fragment(), StartDragListener {
    protected val itemList = ArrayList<CompositeNamedListEntity<E, *>>()
    protected abstract var adapter: A
    protected abstract var itemTouchHelper: ItemTouchHelper

    protected fun setListData(newList: List<CompositeNamedListEntity<E, *>>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }

    fun refreshListData() {
        adapter.dataSet = itemList
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }
}
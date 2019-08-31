package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.CompositeListEntity

abstract class AbstractListFragment<E: AbstractEntity<E>, T: CompositeListEntity<E>, A: CompositeListEntityAdapter<E>>: Fragment(), StartDragListener {
    protected val itemList = ArrayList<T>()
    protected abstract var adapter: A
    protected abstract var itemTouchHelper: ItemTouchHelper

    protected fun setListData(newList: List<T>) {
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
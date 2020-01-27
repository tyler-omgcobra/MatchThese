package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper

abstract class AbstractListFragment<E: Comparable<E>, A: AbstractListEntityAdapter<E, *>>: Fragment() {
    protected val itemList = ArrayList<E>()
    protected abstract var adapter: A
    protected abstract var itemTouchHelper: ItemTouchHelper

    protected fun setListData(newList: List<E>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }

    fun refreshListData() {
        adapter.dataSet = itemList
    }
}
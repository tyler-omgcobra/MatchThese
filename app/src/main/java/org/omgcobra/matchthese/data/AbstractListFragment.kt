package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.NamedEntity

abstract class AbstractListFragment<E: NamedEntity<E>, L: NamedEntity<L>, A: CompositeListEntityAdapter<E, L>>: Fragment() {
    protected val itemList = ArrayList<CompositeNamedListEntity<E, L>>()
    protected abstract var adapter: A
    protected abstract var itemTouchHelper: ItemTouchHelper

    protected fun setListData(newList: List<CompositeNamedListEntity<E, L>>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }

    fun refreshListData() {
        adapter.dataSet = itemList
    }
}
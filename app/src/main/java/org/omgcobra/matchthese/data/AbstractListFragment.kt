package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment

abstract class AbstractListFragment<T: Comparable<T>, A: AbstractComparableAdapter<T, *>>: Fragment() {
    protected val itemList = ArrayList<T>()
    protected abstract var adapter: A

    protected fun setListData(newList: List<T>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }
}
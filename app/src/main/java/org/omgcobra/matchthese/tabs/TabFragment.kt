package org.omgcobra.matchthese.tabs

import androidx.fragment.app.Fragment
import org.omgcobra.matchthese.data.AbstractComparableAdapter

abstract class TabFragment<T: Comparable<T>, A: AbstractComparableAdapter<T, *>>(var title: String = ""): Fragment() {
    protected val itemList = ArrayList<T>()
    protected abstract var adapter: A

    protected fun setListData(newList: List<T>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }

}
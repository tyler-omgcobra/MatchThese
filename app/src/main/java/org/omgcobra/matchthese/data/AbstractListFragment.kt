package org.omgcobra.matchthese.data

import androidx.fragment.app.Fragment
import org.omgcobra.matchthese.model.AbstractEntity
import org.omgcobra.matchthese.model.CompositeListEntity

abstract class AbstractListFragment<E: AbstractEntity<E>, T: CompositeListEntity<E>, A: CompositeListEntityAdapter<E>>: Fragment() {
    protected val itemList = ArrayList<T>()
    protected abstract var adapter: A

    protected fun setListData(newList: List<T>) {
        itemList.clear()
        itemList.addAll(newList)

        adapter.dataSet = itemList
    }
}
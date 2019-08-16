package org.omgcobra.matchthese.tabs.item


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags
import org.omgcobra.matchthese.tabs.SwipeToDeleteCallback
import org.omgcobra.matchthese.tabs.TabFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class ItemsTabFragment(title: String): TabFragment(title) {
    private val itemViewModel: ItemsViewModel by viewModels()
    private lateinit var itemAdapter: ItemAdapter
    private var itemList = ArrayList<ItemWithTags>()
    private var listener: OnListFragmentInteractionListener? = null

    fun setListData(newList: List<ItemWithTags>) {
        itemList.clear()
        itemList.addAll(newList)

        itemAdapter.setItemList(itemList)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context !is OnListFragmentInteractionListener) {
            throw RuntimeException(String.format("%s must implement OnListFragmentInteractionListener", context))
        }

        title = getString(R.string.items_tab_title)
        listener = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_items_tab, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        itemAdapter = ItemAdapter(context)
        itemAdapter.setItemList(itemList)
        recyclerView.adapter = itemAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(itemAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        itemViewModel.getItemsWithTagsList().observe(this, Observer { setListData(it) })
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}

interface OnListFragmentInteractionListener {
    fun onListClickItem(item: Item)
    fun onListFragmentDeleteItem(item: Item)
}
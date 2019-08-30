package org.omgcobra.matchthese.fragments.item


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.data.AbstractListFragment
import org.omgcobra.matchthese.fragments.SwipeToDeleteCallback
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags

/**
 * A simple [Fragment] subclass.
 *
 */
class ItemWithTagsFragment: AbstractListFragment<Item, ItemWithTags, ItemWithTagsAdapter>() {
    private val viewModel: ItemWithTagsViewModel by viewModels()
    override lateinit var adapter: ItemWithTagsAdapter
    override lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_items_view, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.item_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter = ItemWithTagsAdapter(context, this)
        setListData(itemList)
        recyclerView.adapter = adapter

        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getItemsWithTagsList().observe(this, Observer { setListData(it) })
    }
}

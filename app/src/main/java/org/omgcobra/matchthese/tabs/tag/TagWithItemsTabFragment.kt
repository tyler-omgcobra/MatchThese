package org.omgcobra.matchthese.tabs.tag


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.TagWithItems
import org.omgcobra.matchthese.tabs.SwipeToDeleteCallback
import org.omgcobra.matchthese.tabs.TabFragment

/**
 * A simple [Fragment] subclass.
 *
 */
class TagWithItemsTabFragment(title: String) : TabFragment<TagWithItems, TagWithItemsAdapter>(title) {
    private val viewModel: TagWithItemsViewModel by viewModels()
    override lateinit var adapter: TagWithItemsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tags_tab, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.tag_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TagWithItemsAdapter(context)
        setListData(itemList)
        recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getTagsWithItemsList().observe(this, Observer { setListData(it) })
    }
}

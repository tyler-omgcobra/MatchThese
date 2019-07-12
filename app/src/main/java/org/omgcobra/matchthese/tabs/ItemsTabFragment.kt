package org.omgcobra.matchthese.tabs


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import org.omgcobra.matchthese.ItemAdapter
import org.omgcobra.matchthese.MatchTheseApplication
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.ItemWithTags

/**
 * A simple [Fragment] subclass.
 *
 */
class ItemsTabFragment: TabFragment() {
    override val title: String = MatchTheseApplication.getInstance().getString(R.string.items_tab_title)
    private val items = ArrayList<ItemWithTags>()

    private lateinit var itemAdapter: ItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_items_tab, container, false)

        setupRecyclerView(rootView)

        return rootView
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.layoutManager = layoutManager

        val itemsObserver = Observer<List<ItemWithTags>> {
            items.clear()
            items.addAll(it)
        }

        MatchTheseApplication.getDB().itemDao().loadItemsWithTags().observe(this, itemsObserver)


        itemAdapter = ItemAdapter(recyclerView.context, items, object : ItemAdapter.OnItemClickListener {
            override fun onItemClick(item: ItemWithTags) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
        recyclerView.adapter = itemAdapter
    }
}

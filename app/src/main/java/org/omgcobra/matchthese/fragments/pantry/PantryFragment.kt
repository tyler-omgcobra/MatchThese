package org.omgcobra.matchthese.fragments.pantry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.data.AbstractListFragment
import org.omgcobra.matchthese.fragments.SwipeToDeleteCallback
import org.omgcobra.matchthese.model.Ingredient

class PantryFragment : AbstractListFragment<Ingredient, PantryAdapter>() {
    private val viewModel: PantryViewModel by viewModels()
    override lateinit var adapter: PantryAdapter
    override lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_pantry_view, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.pantry_list)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter = PantryAdapter(context)
        setListData(itemList)
        recyclerView.adapter = adapter

        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getPantryList().observe(viewLifecycleOwner, Observer { setListData(it) })
    }
}
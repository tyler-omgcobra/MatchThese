package org.omgcobra.matchthese.fragments.recipe


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.data.AbstractListFragment
import org.omgcobra.matchthese.fragments.SwipeToDeleteCallback
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Ingredient
import org.omgcobra.matchthese.model.Recipe

/**
 * A simple [Fragment] subclass.
 *
 */
class RecipeWithIngredientsFragment: AbstractListFragment<CompositeNamedListEntity<Recipe, Ingredient>, RecipeWithIngredientsAdapter>() {
    private val viewModel: RecipeWithIngredientsViewModel by viewModels()
    override lateinit var adapter: RecipeWithIngredientsAdapter
    override lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_recipes_view, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recipe_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter = RecipeWithIngredientsAdapter(context)
        setListData(itemList)
        recyclerView.adapter = adapter

        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getRecipesWithIngredientsList().observe(viewLifecycleOwner, Observer { setListData(it) })
    }
}

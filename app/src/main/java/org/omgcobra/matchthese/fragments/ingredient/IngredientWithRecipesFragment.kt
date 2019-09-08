package org.omgcobra.matchthese.fragments.ingredient
import android.os.Bundle
import androidx.fragment.app.Fragment
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
import org.omgcobra.matchthese.model.Recipe
import org.omgcobra.matchthese.model.Ingredient

/**
 * A simple [Fragment] subclass.
 *
 */
class IngredientWithRecipesFragment : AbstractListFragment<Ingredient, Recipe, IngredientWithRecipesAdapter>() {
    private val viewModel: IngredientWithRecipesViewModel by viewModels()
    override lateinit var adapter: IngredientWithRecipesAdapter
    override lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_ingredients_view, container, false)
        val context = rootView.context
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.ingredient_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        adapter = IngredientWithRecipesAdapter(context)
        setListData(itemList)
        recyclerView.adapter = adapter

        itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.getIngredientsWithRecipesList().observe(this, Observer { setListData(it) })
    }
}

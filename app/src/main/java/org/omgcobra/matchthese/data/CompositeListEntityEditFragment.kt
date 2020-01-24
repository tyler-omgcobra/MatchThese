package org.omgcobra.matchthese.data

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.fragments.SwipeToDeleteCallback
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.NamedEntity
import org.omgcobra.matchthese.model.RecipeIngredientJoin
import java.math.BigDecimal

abstract class CompositeListEntityEditFragment<E: NamedEntity<E>, L: NamedEntity<L>>: Fragment() {
    protected lateinit var nameEditText: EditText
    protected lateinit var listRecyclerView: RecyclerView
    protected lateinit var rowEditAdapter: RowEditAdapter<L>
    protected var listEntity: CompositeNamedListEntity<E, L>? = null
    private lateinit var entitySavedListener: ListEntitySavedListener

    protected abstract val hintId: Int
    protected abstract val liveData: LiveData<List<L>>

    abstract fun createRowEditAdapter(entityAdapter: ArrayAdapter<L>): RowEditAdapter<L>
    abstract fun getEntity(name: String): E
    abstract fun makeListEntity(entity: E): CompositeNamedListEntity<E, L>
    abstract fun getListEntityName(join: RecipeIngredientJoin): String
    abstract fun removeFromListEntity(name: String)
    abstract fun addToListEntity(name: String, amount: BigDecimal, unit: String)
    abstract fun insertEntity(entity: E)
    abstract fun updateEntity(entity: E)

    interface ListEntitySavedListener {
        fun updateListData()
    }

    private fun saveEntity(entity: E) {
        if (listEntity != null) {
            updateEntity(entity)
        } else {
            listEntity = makeListEntity(entity)
            insertEntity(entity)
        }
    }

    private fun saveListData() {
        listEntity!!.joinList.filter { join -> !rowEditAdapter.dataSet.any { getListEntityName(it) == getListEntityName(join) } }
                .forEach { removeFromListEntity(getListEntityName(it)) }
        rowEditAdapter.dataSet.forEach { addToListEntity(getListEntityName(it), it.amount, it.unit) }
    }

    private fun saveItem() {
        val name = nameEditText.text.toString()
        val entity = getEntity(name)
        entity.name = name

        saveEntity(entity)

        saveListData()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
        entitySavedListener.updateListData()
        findNavController().popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val saveItem = menu.findItem(R.id.action_save)
        saveItem.isVisible = true
        saveItem.setOnMenuItemClickListener {
            saveItem()
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            entitySavedListener = context as ListEntitySavedListener
        } catch (ex: ClassCastException) {
            throw ClassCastException("%s must implement ListEntitySavedListener".format(context.toString()))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_edit, container, false)

        listEntity = arguments?.get("listEntity") as CompositeNamedListEntity<E, L>?
        nameEditText = rootView.findViewById(R.id.edit_name)
        listRecyclerView = rootView.findViewById(R.id.edit_recycler_view)
        listRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        listRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))

        val entityAdapter = ArrayAdapter<L>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        liveData.observe(this, Observer {
            entityAdapter.clear()
            entityAdapter.addAll(it)
        })
        rowEditAdapter = createRowEditAdapter(entityAdapter)
        rowEditAdapter.dataSet = ArrayList(listEntity?.joinList ?: emptyList())
        listRecyclerView.adapter = rowEditAdapter

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(rowEditAdapter))
        itemTouchHelper.attachToRecyclerView(listRecyclerView)

        nameEditText.setText(listEntity?.entity?.name)

        return rootView
    }
}

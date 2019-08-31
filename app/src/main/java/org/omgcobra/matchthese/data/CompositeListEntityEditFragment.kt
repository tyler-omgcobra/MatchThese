package org.omgcobra.matchthese.data

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.CompositeListEntity
import java.lang.ClassCastException

abstract class CompositeListEntityEditFragment<T: CompositeListEntity<*>>: Fragment() {
    protected lateinit var nameEditText: EditText
    protected lateinit var listEditText: MultiAutoCompleteTextView
    protected var listEntity: T? = null
    private lateinit var entitySavedListener: ListEntitySavedListener

    abstract val hintId: Int

    protected abstract fun initEntity(view: View)
    protected open fun saveItem() {
        entitySavedListener.updateListData()
        findNavController().popBackStack()
    }

    interface ListEntitySavedListener {
        fun updateListData()
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

        listEntity = arguments?.get("listEntity") as T?
        nameEditText = rootView.findViewById(R.id.edit_name)
        listEditText = rootView.findViewById(R.id.edit_list)
        listEditText.setHint(hintId)

        initEntity(rootView)

        return rootView
    }
}
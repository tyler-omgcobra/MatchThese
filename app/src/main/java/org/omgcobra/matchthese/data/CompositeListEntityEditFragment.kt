package org.omgcobra.matchthese.data

import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.model.CompositeListEntity

abstract class CompositeListEntityEditFragment<T: CompositeListEntity<*>>: Fragment() {
    protected lateinit var nameEditText: EditText
    protected lateinit var listEditText: MultiAutoCompleteTextView
    protected var listEntity: T? = null

    abstract val hintId: Int

    protected abstract fun initEntity(view: View)
    protected abstract fun saveItem()

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
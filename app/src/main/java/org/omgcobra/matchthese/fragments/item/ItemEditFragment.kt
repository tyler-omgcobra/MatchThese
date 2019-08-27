package org.omgcobra.matchthese.fragments.item

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags

class ItemEditFragment: Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var tagsEditText: MultiAutoCompleteTextView
    private var itemWithTags: ItemWithTags? = null

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
        val rootView = inflater.inflate(R.layout.fragment_edit_item, container, false)

        itemWithTags = arguments?.get("itemWithTags") as ItemWithTags?

        nameEditText = rootView.findViewById(R.id.item_edit_name)
        nameEditText.setText(itemWithTags?.entity?.name)

//        val tags = ItemRepository.getTagsWithItems().value!!.map { it.tag.name }
        val tags = listOf("cool", "uncool")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags)
        tagsEditText = rootView.findViewById(R.id.item_edit_tags)
        tagsEditText.setAdapter(adapter)
        tagsEditText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        tagsEditText.setText(itemWithTags?.list?.joinToString { it })

        return rootView
    }

    private fun saveItem() {
        val name = nameEditText.text.toString()
        val tags = tagsEditText.text.toString().split(Regex(", *"))
        val item = itemWithTags?.entity ?: Item(name)

        if (itemWithTags != null) {
            ItemRepository.update(item)
        } else {
            itemWithTags = ItemWithTags(item)
            ItemRepository.insert(item)
        }

        for (tag in tags) {
            ItemRepository.ensureTagOnItem(itemWithTags!!, tag)
        }

        findNavController().popBackStack()
    }
}
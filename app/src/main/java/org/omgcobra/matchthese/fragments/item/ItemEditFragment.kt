package org.omgcobra.matchthese.fragments.item

import android.view.*
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags

class ItemEditFragment: CompositeListEntityEditFragment<ItemWithTags>() {
    override val hintId = R.string.tags

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val tags = listOf("cool", "uncool")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, tags)
        listEditText.setAdapter(adapter)
        listEditText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        listEditText.setText(listEntity?.list?.joinToString { it })
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val tags = listEditText.text.toString().split(Regex(", *"))
        val item = listEntity?.entity ?: Item(name)
        item.name = name

        if (listEntity != null) {
            ItemRepository.update(item)
        } else {
            listEntity = ItemWithTags(item)
            ItemRepository.insert(item)
        }

        for (tag in tags) {
            if (tag.isNotEmpty()) ItemRepository.ensureTagOnItem(listEntity!!, tag)
        }

        super.saveItem()
    }
}
package org.omgcobra.matchthese.fragments.tag

import android.view.*
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.navigation.fragment.findNavController
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Tag
import org.omgcobra.matchthese.model.TagWithItems

class TagEditFragment: CompositeListEntityEditFragment<TagWithItems>() {
    override val hintId = R.string.items

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val items = listOf("Tyler", "Stephanie")
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, items)
        listEditText.setAdapter(adapter)
        listEditText.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        listEditText.setText(listEntity?.list?.joinToString { it })
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val items = listEditText.text.toString().split(Regex(", *"))
        val tag = listEntity?.entity ?: Tag(name)
        tag.name = name

        if (listEntity != null) {
            ItemRepository.update(tag)
        } else {
            listEntity = TagWithItems(tag)
            ItemRepository.insert(tag)
        }

        for (item in items) {
            if (item.isNotEmpty()) ItemRepository.ensureItemInTag(listEntity!!, item)
        }

        findNavController().popBackStack()
    }
}
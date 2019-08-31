package org.omgcobra.matchthese.fragments.tag

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.Tag
import org.omgcobra.matchthese.model.TagWithItems

class TagEditFragment: CompositeListEntityEditFragment<TagWithItems>() {
    override val hintId = R.string.items

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val adapter = createAdapter()
        setupListEditText(adapter)
        ItemRepository.getAll<Item>().observe(this) { itemList ->
            adapter.clear()
            adapter.addAll(itemList.map { it.name })
        }
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

        listEntity!!.list.filter { !items.contains(it) }
                .forEach { ItemRepository.removeItemFromTag(listEntity!!, it) }

        items.filter { it.isNotEmpty() }
                .forEach { ItemRepository.ensureItemInTag(listEntity!!, it) }

        super.saveItem()
    }
}
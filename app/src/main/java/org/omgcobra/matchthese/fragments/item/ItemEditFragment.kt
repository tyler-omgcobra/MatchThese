package org.omgcobra.matchthese.fragments.item

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags
import org.omgcobra.matchthese.model.Tag

class ItemEditFragment: CompositeListEntityEditFragment<Item, Tag>() {
    override val hintId = R.string.tags
    override val layoutId = R.layout.fragment_edit_item

    override fun initEntity(view: View) {
        val adapter = createAdapter()
        setupListEditText(adapter)
        listEditText.setText("%s,".format(listEntity?.joinList?.joinToString { it.tag.toString() }))
        listEntity?.joinList?.forEach {
            listEditText.addObjectSync(it.tag)
        }
        ItemRepository.getAll<Tag>().observe(this) { tagList ->
            adapter.clear()
            adapter.addAll(tagList)
        }
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val tags = listEditText.objects
        val item = listEntity?.entity ?: Item(name)
        item.name = name

        if (listEntity != null) {
            ItemRepository.update(item)
        } else {
            listEntity = ItemWithTags(item)
            ItemRepository.insert(item)
        }

        listEntity!!.joinList.filter { !tags.contains(it.tag) }
                .forEach { ItemRepository.removeTagFromItem(listEntity!!, it.tag!!.name) }

        tags.forEach { ItemRepository.ensureTagOnItem(listEntity!!, it.name) }

        super.saveItem()
    }
}
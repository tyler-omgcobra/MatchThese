package org.omgcobra.matchthese.fragments.tag

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.Tag
import org.omgcobra.matchthese.model.TagWithItems

class TagEditFragment: CompositeListEntityEditFragment<Tag, Item>() {
    override val hintId = R.string.items
    override val layoutId = R.layout.fragment_edit_tag

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val adapter = createAdapter()
        setupListEditText(adapter)
        listEditText.setText("%s,".format(listEntity?.joinList?.joinToString { it.item.toString() }))
        listEntity?.joinList?.forEach {
            listEditText.addObjectSync(it.item)
        }
        ItemRepository.getAll<Item>().observe(this) { itemList ->
            adapter.clear()
            adapter.addAll(itemList)
        }
    }

    override fun saveItem() {
        val name = nameEditText.text.toString()
        val items = listEditText.objects
        val tag = listEntity?.entity ?: Tag(name)
        tag.name = name

        if (listEntity != null) {
            ItemRepository.update(tag)
        } else {
            listEntity = TagWithItems(tag)
            ItemRepository.insert(tag)
        }

        listEntity!!.joinList.filter { !items.contains(it.item) }
                .forEach { ItemRepository.removeItemFromTag(listEntity!!, it.item.name) }

        items.forEach { ItemRepository.ensureItemInTag(listEntity!!, it.name) }

        super.saveItem()
    }
}
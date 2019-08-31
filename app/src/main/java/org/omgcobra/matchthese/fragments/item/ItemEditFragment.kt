package org.omgcobra.matchthese.fragments.item

import android.view.*
import androidx.lifecycle.observe
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityEditFragment
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.ItemWithTags
import org.omgcobra.matchthese.model.Tag

class ItemEditFragment: CompositeListEntityEditFragment<ItemWithTags>() {
    override val hintId = R.string.tags

    override fun initEntity(view: View) {
        nameEditText.setText(listEntity?.entity?.name)
        val adapter = createAdapter()
        setupListEditText(adapter)
        ItemRepository.getAll<Tag>().observe(this) { tagList ->
            adapter.clear()
            adapter.addAll(tagList.map { it.name })
        }
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
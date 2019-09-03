package org.omgcobra.matchthese.fragments.tag

import android.content.Context
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Item
import org.omgcobra.matchthese.model.Tag

class TagWithItemsAdapter(context: Context) : CompositeListEntityAdapter<Tag, Item>(context) {
    override val editActionId = R.id.edit_tag

    override fun getSubText(listEntity: CompositeNamedListEntity<Tag, *>) = listEntity.joinList.joinToString { it.item.name }

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.entity)
    }
}
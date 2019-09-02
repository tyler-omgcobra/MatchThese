package org.omgcobra.matchthese.fragments.item

import android.content.Context
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.data.StartDragListener
import org.omgcobra.matchthese.model.CompositeNamedListEntity
import org.omgcobra.matchthese.model.Item

class ItemWithTagsAdapter(context: Context, dragListener: StartDragListener) : CompositeListEntityAdapter<Item>(context, dragListener) {
    override val editActionId = R.id.edit_item

    override fun getSubText(listEntity: CompositeNamedListEntity<Item, *>) = listEntity.joinList.joinToString { it.tag!!.toString() }

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.entity)
    }
}
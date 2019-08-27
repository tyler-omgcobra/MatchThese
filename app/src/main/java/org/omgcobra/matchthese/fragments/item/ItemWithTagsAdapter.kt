package org.omgcobra.matchthese.fragments.item

import android.content.Context
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.model.Item

class ItemWithTagsAdapter(context: Context) : CompositeListEntityAdapter<Item>(context) {

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.entity)
    }
}
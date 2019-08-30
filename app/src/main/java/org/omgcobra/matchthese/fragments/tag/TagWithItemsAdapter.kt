package org.omgcobra.matchthese.fragments.tag

import android.content.Context
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.CompositeListEntityAdapter
import org.omgcobra.matchthese.data.StartDragListener
import org.omgcobra.matchthese.model.Tag

class TagWithItemsAdapter(context: Context, dragListener: StartDragListener) : CompositeListEntityAdapter<Tag>(context, dragListener) {
    override val editActionId = R.id.edit_tag

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.entity)
    }
}
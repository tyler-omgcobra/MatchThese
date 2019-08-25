package org.omgcobra.matchthese.fragments.item

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.AbstractComparableAdapter
import org.omgcobra.matchthese.model.ItemWithTags

class ItemWithTagsAdapter(context: Context) : AbstractComparableAdapter<ItemWithTags, ItemWithTagsAdapter.ItemWithTagsViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemWithTagsViewHolder {
        return ItemWithTagsViewHolder(View.inflate(context, R.layout.item_view_item_with_tags, null))
    }

    override fun onBindViewHolder(holder: ItemWithTagsViewHolder, position: Int) {
        val itemWithTags = dataSet[position]
        holder.itemText.text = itemWithTags.item.name
        holder.itemTags.text = itemWithTags.tagList.joinToString { it }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked " + itemWithTags.item.name, Toast.LENGTH_SHORT).show()
            ItemRepository.ensureTagOnItem(itemWithTags, "cool")
        }
    }

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.item)
    }

    class ItemWithTagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.item_name)
        val itemTags: TextView = itemView.findViewById(R.id.item_tags)
    }
}
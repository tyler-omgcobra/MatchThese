package org.omgcobra.matchthese.tabs.tag

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.AbstractComparableAdapter
import org.omgcobra.matchthese.model.TagWithItems

class TagWithItemsAdapter(context: Context) : AbstractComparableAdapter<TagWithItems, TagWithItemsAdapter.TagWithItemsViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagWithItemsViewHolder {
        return TagWithItemsViewHolder(View.inflate(context, R.layout.tag_view_tags_with_items, null))
    }

    override fun onBindViewHolder(holder: TagWithItemsViewHolder, position: Int) {
        val itemWithTags = dataSet[position]
        holder.itemText.text = itemWithTags.tag.name
        holder.itemTags.text = itemWithTags.itemList.joinToString { it }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked " + itemWithTags.tag.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun delete(position: Int) {
        super.delete(position)

        ItemRepository.delete(deletedItem!!.tag)
    }

    class TagWithItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.tag_name)
        val itemTags: TextView = itemView.findViewById(R.id.tag_items)
    }
}
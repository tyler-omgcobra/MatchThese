package org.omgcobra.matchthese.fragments.tag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.data.AbstractComparableAdapter
import org.omgcobra.matchthese.model.TagWithItems

class TagWithItemsAdapter(context: Context) : AbstractComparableAdapter<TagWithItems, TagWithItemsAdapter.TagWithItemsViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagWithItemsViewHolder {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return TagWithItemsViewHolder(inflater.inflate(R.layout.tag_view_tags_with_items, parent, false))
    }

    override fun onBindViewHolder(holder: TagWithItemsViewHolder, position: Int) {
        val tagWithItems = dataSet[position]
        holder.itemText.text = tagWithItems.tag.name
        holder.itemTags.text = tagWithItems.itemList.sorted().joinToString { it }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked " + tagWithItems.tag.name, Toast.LENGTH_SHORT).show()
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
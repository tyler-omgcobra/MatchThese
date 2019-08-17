package org.omgcobra.matchthese.tabs.item

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.ItemWithTags
import org.omgcobra.matchthese.model.Tag

class ItemAdapter(internal val context: Context) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private lateinit var dataSet: List<ItemWithTags>
    private var deletedItem: ItemWithTags? = null
    private var deletedItemPosition: Int? = null

    fun setItemList(itemList: List<ItemWithTags>) {
        this.dataSet = itemList.sorted()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(View.inflate(context, R.layout.item_view_item_with_tags, null))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemWithTags = dataSet[position]
        holder.itemText.text = itemWithTags.item.name
        holder.itemTags.text = itemWithTags.tagList.joinToString { it }
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked " + itemWithTags.item.name, Toast.LENGTH_SHORT).show()
            val tag = Tag("cool")
            if (ItemRepository.getAll<Tag>().value.orEmpty().isEmpty()) {
                ItemRepository.insert(tag).get()
            }
            if (!itemWithTags.tagList.contains("cool")) {
                ItemRepository.addTagToItem(itemWithTags.item, tag)
            }
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun deleteItem(position: Int) {
        deletedItem = dataSet[position]
        deletedItemPosition = position
        ItemRepository.delete(dataSet[position].item)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.item_name)
        val itemTags: TextView = itemView.findViewById(R.id.item_tags)
    }

}
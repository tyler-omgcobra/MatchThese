package org.omgcobra.matchthese.tabs.item

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import org.omgcobra.matchthese.R
import org.omgcobra.matchthese.dao.ItemRepository
import org.omgcobra.matchthese.model.ItemWithTags

class ItemAdapter(internal val context: Context) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private lateinit var dataSet: List<ItemWithTags>
    private var deletedItem: ItemWithTags? = null
    private var deletedItemPosition: Int? = null

    fun setItemList(itemList: List<ItemWithTags>) {
        this.dataSet = itemList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(View.inflate(context, R.layout.item_view_item_with_tags, null))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataSet[position]
        holder.itemText.text = item.item.name
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "clicked " + item.item.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun deleteItem(position: Int) {
        deletedItem = dataSet[position]
        deletedItemPosition = position
        ItemRepository().deleteItem(dataSet[position].item)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemText: TextView = itemView.findViewById(R.id.item_name)
    }

}
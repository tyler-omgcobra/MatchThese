package org.omgcobra.matchthese

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.omgcobra.matchthese.model.ItemWithTags

class ItemAdapter(private val context: Context,
                  private val dataSet: List<ItemWithTags>,
                  private val listener: OnItemClickListener) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    private var selectedItem: ItemWithTags? = null

    var selectedItemIndex: Int
        get() = dataSet.indexOf(selectedItem)
        set(index) {
            this.selectedItem = dataSet[index]
        }

    interface OnItemClickListener {
        fun onItemClick(item: ItemWithTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(android.R.layout.simple_selectable_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].item.name
        holder.title.textSize = 22f
        holder.itemView.setOnClickListener {
            listener.onItemClick(dataSet[position])
            selectedItem = dataSet[position]
            notifyDataSetChanged()
        }
        if (dataSet[position] == selectedItem) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_dark))
            holder.title.setTextColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.title.setTextColor(Color.BLACK)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var title: TextView = v.findViewById<View>(android.R.id.text1) as TextView
    }
}
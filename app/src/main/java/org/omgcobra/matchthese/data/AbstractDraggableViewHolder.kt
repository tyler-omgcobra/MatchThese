package org.omgcobra.matchthese.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view_item_with_tags.view.*
import org.omgcobra.matchthese.R

abstract class AbstractDraggableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val dragHandle: ImageView = itemView.findViewById(R.id.handle)
}
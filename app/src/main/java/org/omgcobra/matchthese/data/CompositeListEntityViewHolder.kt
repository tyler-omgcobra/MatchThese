package org.omgcobra.matchthese.data

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.R

class CompositeListEntityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val mainText: TextView = itemView.findViewById(R.id.main_text)
    val subText: TextView = itemView.findViewById(R.id.sub_text)
    val img: ImageView = itemView.findViewById(R.id.item_image)
}
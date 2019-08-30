package org.omgcobra.matchthese.data

import androidx.recyclerview.widget.RecyclerView

interface StartDragListener {
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
}
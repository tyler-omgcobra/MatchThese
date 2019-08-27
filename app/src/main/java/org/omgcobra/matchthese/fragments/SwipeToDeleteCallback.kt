package org.omgcobra.matchthese.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import org.omgcobra.matchthese.data.CompositeListEntityAdapter

class SwipeToDeleteCallback(private val adapter: CompositeListEntityAdapter<*>): ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.START or ItemTouchHelper.END) {
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = adapter.onItemSwipe(viewHolder.adapterPosition)

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView

            val p = Paint()
            p.color = Color.parseColor("#D32F2F")

            if (dX > 0) {
                c.drawRect(RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat()), p)
            } else if (dX < 0) {
                c.drawRect(RectF(itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat()), p)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}

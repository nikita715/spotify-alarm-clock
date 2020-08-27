package ru.nikstep.alarm.ui.main.alarms

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class AlarmItemTouchHelperCallback(
    private val onSwipeListener: OnSwipeListener
) : ItemTouchHelper.SimpleCallback(
    0,
    makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.END)
) {

    private val paint: Paint = Paint()

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.END) {
            onSwipeListener.onSwipeEnd(viewHolder)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView: View = viewHolder.itemView

        if (itemView.height > 0 && dX > 0) {
            val left = itemView.left.toFloat()
            val top = itemView.top.toFloat()
            val bottom = itemView.bottom.toFloat()
            paint.color = Color.RED
            val background = RectF(left, top, dX, bottom)
            c.drawRect(background, paint)
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    fun interface OnSwipeListener {
        fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder)
    }
}
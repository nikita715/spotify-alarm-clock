package ru.nikstep.alarm.ui.main.alarms

import android.graphics.Paint
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

    interface OnSwipeListener {
        fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder)
    }
}
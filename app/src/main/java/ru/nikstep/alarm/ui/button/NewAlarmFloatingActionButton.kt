package ru.nikstep.alarm.ui.button

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NewAlarmFloatingActionButton(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior() {

    override fun onAttachedToLayoutParams(lp: CoordinatorLayout.LayoutParams) {
        if (lp.dodgeInsetEdges == Gravity.NO_GRAVITY) {
            lp.dodgeInsetEdges = Gravity.BOTTOM
        }
    }
}
package com.dvdb.materialchecklist.util

import android.text.Selection
import android.text.Spannable
import android.text.method.ArrowKeyMovementMethod
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.widget.TextView

internal object LinksMovementMethod : ArrowKeyMovementMethod() {

    override fun onTouchEvent(
        widget: TextView,
        buffer: Spannable,
        event: MotionEvent
    ): Boolean {
        val action = event.action
        if (action == MotionEvent.ACTION_UP ||
            action == MotionEvent.ACTION_DOWN
        ) {
            val x = event.x.toInt() - widget.totalPaddingLeft + widget.scrollX
            val y = event.y.toInt() - widget.totalPaddingTop + widget.scrollY
            val line = widget.layout.getLineForVertical(y)
            val horizontalOffset = widget.layout.getOffsetForHorizontal(
                line,
                x.toFloat()
            )

            val link = buffer.getSpans(
                horizontalOffset,
                horizontalOffset,
                ClickableSpan::class.java
            )

            if (link.isNotEmpty()) {
                if (action == MotionEvent.ACTION_UP) {
                    link.first().onClick(widget)
                } else {
                    Selection.setSelection(
                        buffer,
                        buffer.getSpanStart(link.first()),
                        buffer.getSpanEnd(link.first())
                    )
                }
                return true
            }
        }
        return super.onTouchEvent(widget, buffer, event)
    }
}
package com.dvdb.checklist.recycler.holder.util

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

internal class EnterActionPerformedFactory {

    fun create(onEnterActionPerformed: () -> Unit) = TextView.OnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_NEXT || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
            onEnterActionPerformed()
        }
        return@OnEditorActionListener true
    }
}
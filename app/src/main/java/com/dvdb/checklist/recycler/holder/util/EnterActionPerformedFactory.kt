package com.dvdb.checklist.recycler.holder.util

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

internal class EnterActionPerformedFactory {

    fun create(
        runnable: () -> Boolean,
        preConditions: () -> Boolean = { false }
    ) = TextView.OnEditorActionListener { _, actionId, event ->
        if (preConditions()) {
            return@OnEditorActionListener false
        }

        if (event == null) {
            if (actionId != EditorInfo.IME_ACTION_NEXT && actionId != EditorInfo.IME_ACTION_DONE) {
                return@OnEditorActionListener false
            }
        } else if (actionId == EditorInfo.IME_NULL || actionId == KeyEvent.KEYCODE_ENTER) {
            if (event.action != KeyEvent.ACTION_DOWN) {
                return@OnEditorActionListener true
            }
        } else {
            return@OnEditorActionListener false
        }

        return@OnEditorActionListener runnable()
    }
}
package com.dvdb.checklist.recycler.holder.util

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo

internal class EnterKeyListenerFactory {

    fun create(onEnterKeyPressed: () -> Unit) = View.OnKeyListener { _, keyCode, event ->
        return@OnKeyListener if ((keyCode == KeyEvent.KEYCODE_ENTER || keyCode == EditorInfo.IME_NULL) && event.action == KeyEvent.ACTION_DOWN) {
            onEnterKeyPressed()
            true
        } else {
            false
        }
    }
}
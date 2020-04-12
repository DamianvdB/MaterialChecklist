package com.dvdb.checklist.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

internal fun View.setVisible(
    isVisible: Boolean,
    hiddenVisibility: Int = View.GONE
) {
    require(hiddenVisibility == View.GONE || hiddenVisibility == View.INVISIBLE)
    visibility = if (isVisible) View.VISIBLE else hiddenVisibility
}

internal fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

internal fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, 0)
}
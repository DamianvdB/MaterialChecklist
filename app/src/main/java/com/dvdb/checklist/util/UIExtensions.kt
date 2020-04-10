package com.dvdb.checklist.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

internal fun View.setVisible(isVisible: Boolean, hiddenVisibility: Int = View.GONE) {
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

internal fun Drawable.setTintCompat(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
}

@ColorInt
internal fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}
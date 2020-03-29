package com.dvdb.checklist.util

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

internal fun View.setVisible(isVisible: Boolean, hiddenVisibility: Int = View.GONE) {
    require(hiddenVisibility == View.GONE || hiddenVisibility == View.INVISIBLE)
    visibility = if (isVisible) View.VISIBLE else hiddenVisibility
}

internal fun Drawable.setTintCompat(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
}
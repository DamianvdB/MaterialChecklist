package com.dvdb.checklist.util

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

internal fun Drawable.setTintCompat(@ColorInt color: Int) {
    DrawableCompat.setTint(this, color)
}
package com.dvdb.materialchecklist.util

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

internal fun Drawable.setTintCompat(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        DrawableCompat.setTint(this, color)
    } else {
        DrawableCompat.setTint(DrawableCompat.wrap(this), color)
    }
}
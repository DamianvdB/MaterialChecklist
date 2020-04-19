package com.dvdb.materialchecklist.util

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

internal fun Drawable.setTintCompat(@ColorInt color: Int) {
    // Clear existing tint before applying new tint color
    DrawableCompat.setTintList(this, null)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        DrawableCompat.setTint(this, color)
    } else {
        DrawableCompat.setTint(DrawableCompat.wrap(this), color)
    }
}
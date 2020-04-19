package com.dvdb.materialchecklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.MaterialChecklist

/**
 * Text configuration
 */
fun MaterialChecklist.setTextColor(@ColorInt textColor: Int): MaterialChecklist {
    if (config.textColor != textColor) {
        config.textColor = textColor
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setTextSize(@Px textSize: Float): MaterialChecklist {
    if (config.textSize != textSize) {
        config.textSize = textSize
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setNewItemText(text: String): MaterialChecklist {
    if (config.textNewItem != text) {
        config.textNewItem = text
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setCheckedItemTextAlpha(alpha: Float): MaterialChecklist {
    if (config.textAlphaCheckedItem != alpha) {
        config.textAlphaCheckedItem = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setNewItemTextAlpha(alpha: Float): MaterialChecklist {
    if (config.textAlphaNewItem != alpha) {
        config.textAlphaNewItem = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setTextTypeFace(typeface: Typeface): MaterialChecklist {
    if (config.textTypeFace != typeface) {
        config.textTypeFace = typeface
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

/**
 * Icon configuration
 */
fun MaterialChecklist.setIconTintColor(@ColorInt tintColor: Int): MaterialChecklist {
    if (config.iconTintColor != tintColor) {
        config.iconTintColor = tintColor
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setDragIndicatorIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaDragIndicator != alpha) {
        config.iconAlphaDragIndicator = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setDeleteIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaDelete != alpha) {
        config.iconAlphaDelete = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setAddIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaAdd != alpha) {
        config.iconAlphaAdd = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

/**
 * Checkbox configuration
 */
fun MaterialChecklist.setCheckboxTintColor(@ColorInt tintColor: Int): MaterialChecklist {
    if (config.checkboxTintColor != tintColor) {
        config.checkboxTintColor = tintColor
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setCheckedItemCheckboxAlpha(alpha: Float): MaterialChecklist {
    if (config.checkboxAlphaCheckedItem != alpha) {
        config.checkboxAlphaCheckedItem = alpha
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

/**
 * Drag-and-drop configuration
 */
fun MaterialChecklist.setDragAndDropEnabled(isEnabled: Boolean): MaterialChecklist {
    if (config.dragAndDropEnabled != isEnabled) {
        config.dragAndDropEnabled = isEnabled
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setDragAndDropItemActiveBackgroundColor(@ColorInt backgroundColor: Int): MaterialChecklist {
    if (config.dragAndDropActiveItemBackgroundColor != backgroundColor) {
        config.dragAndDropActiveItemBackgroundColor = backgroundColor
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

/**
 * Behavior configuration
 */
fun MaterialChecklist.setOnItemCheckedBehavior(behavior: BehaviorCheckedItem): MaterialChecklist {
    if (config.behaviorCheckedItem != behavior) {
        config.behaviorCheckedItem = behavior
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

fun MaterialChecklist.setOnItemUncheckedBehavior(behavior: BehaviorUncheckedItem): MaterialChecklist {
    if (config.behaviorUncheckedItem != behavior) {
        config.behaviorUncheckedItem = behavior
        manager.setConfig(config.toManagerConfig())
    }
    return this
}

/**
 * Item configuration
 */
fun MaterialChecklist.setItemHorizontalPadding(@Px padding: Float): MaterialChecklist {
    if (config.itemHorizontalPadding != padding) {
        config.itemHorizontalPadding = padding
        manager.setConfig(config.toManagerConfig())
    }
    return this
}
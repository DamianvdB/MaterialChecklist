package com.dvdb.checklist.config

import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.Checklist

/**
 * Text configuration
 */
fun Checklist.setTextColor(@ColorInt textColor: Int): Checklist {
    config.textColor = textColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setTextSize(@Px textSize: Float): Checklist {
    config.textSize = textSize
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setNewItemText(text: String): Checklist {
    config.textNewItem = text
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setCheckedItemTextAlpha(alpha: Float): Checklist {
    config.textAlphaCheckedItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setNewItemTextAlpha(alpha: Float): Checklist {
    config.textAlphaNewItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Icon configuration
 */
fun Checklist.setIconTintColor(@ColorInt tintColor: Int): Checklist {
    config.iconTintColor = tintColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setDragIndicatorIconAlpha(alpha: Float): Checklist {
    config.iconAlphaDragIndicator = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setDeleteIconAlpha(alpha: Float): Checklist {
    config.iconAlphaDelete = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setAddIconAlpha(alpha: Float): Checklist {
    config.iconAlphaAdd = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Checkbox configuration
 */
fun Checklist.setCheckboxTintColor(@ColorInt tintColor: Int): Checklist {
    config.checkboxTintColor = tintColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setCheckedItemCheckboxAlpha(alpha: Float): Checklist {
    config.checkboxAlphaCheckedItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Drag-and-drop configuration
 */
fun Checklist.setDragAndDropEnabled(isEnabled: Boolean): Checklist {
    config.dragAndDropEnabled = isEnabled
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setDragAndDropItemActiveBackgroundColor(@ColorInt backgroundColor: Int): Checklist {
    config.dragAndDropActiveItemBackgroundColor = backgroundColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun Checklist.setDragAndDropItemActiveElevation(elevation: Float): Checklist {
    config.dragAndDropActiveItemElevation = elevation
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Checked item behavior configuration
 */
fun Checklist.setOnItemCheckedBehavior(behavior: CheckedItemBehavior): Checklist {
    config.behaviorCheckedItem = behavior
    manager.setConfig(config.toManagerConfig())
    return this
}
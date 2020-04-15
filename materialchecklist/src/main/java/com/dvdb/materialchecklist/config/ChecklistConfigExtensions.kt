package com.dvdb.materialchecklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.MaterialChecklist

/**
 * Text configuration
 */
fun MaterialChecklist.setTextColor(@ColorInt textColor: Int): MaterialChecklist {
    config.textColor = textColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setTextSize(@Px textSize: Float): MaterialChecklist {
    config.textSize = textSize
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setNewItemText(text: String): MaterialChecklist {
    config.textNewItem = text
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setCheckedItemTextAlpha(alpha: Float): MaterialChecklist {
    config.textAlphaCheckedItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setNewItemTextAlpha(alpha: Float): MaterialChecklist {
    config.textAlphaNewItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setTextTypeFace(typeface: Typeface): MaterialChecklist {
    config.textTypeFace = typeface
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Icon configuration
 */
fun MaterialChecklist.setIconTintColor(@ColorInt tintColor: Int): MaterialChecklist {
    config.iconTintColor = tintColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setDragIndicatorIconAlpha(alpha: Float): MaterialChecklist {
    config.iconAlphaDragIndicator = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setDeleteIconAlpha(alpha: Float): MaterialChecklist {
    config.iconAlphaDelete = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setAddIconAlpha(alpha: Float): MaterialChecklist {
    config.iconAlphaAdd = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Checkbox configuration
 */
fun MaterialChecklist.setCheckboxTintColor(@ColorInt tintColor: Int): MaterialChecklist {
    config.checkboxTintColor = tintColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setCheckedItemCheckboxAlpha(alpha: Float): MaterialChecklist {
    config.checkboxAlphaCheckedItem = alpha
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Drag-and-drop configuration
 */
fun MaterialChecklist.setDragAndDropEnabled(isEnabled: Boolean): MaterialChecklist {
    config.dragAndDropEnabled = isEnabled
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setDragAndDropItemActiveBackgroundColor(@ColorInt backgroundColor: Int): MaterialChecklist {
    config.dragAndDropActiveItemBackgroundColor = backgroundColor
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setDragAndDropItemActiveElevation(@Px elevation: Float): MaterialChecklist {
    config.dragAndDropActiveItemElevation = elevation
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Behavior configuration
 */
fun MaterialChecklist.setOnItemCheckedBehavior(behavior: BehaviorCheckedItem): MaterialChecklist {
    config.behaviorCheckedItem = behavior
    manager.setConfig(config.toManagerConfig())
    return this
}

fun MaterialChecklist.setOnItemUncheckedBehavior(behavior: BehaviorUncheckedItem): MaterialChecklist {
    config.behaviorUncheckedItem = behavior
    manager.setConfig(config.toManagerConfig())
    return this
}

/**
 * Item configuration
 */
fun MaterialChecklist.setItemHorizontalPadding(@Px padding: Float): MaterialChecklist {
    config.itemHorizontalPadding = padding
    manager.setConfig(config.toManagerConfig())
    return this
}
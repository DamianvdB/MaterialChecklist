/*
 * Designed and developed by Damian van den Berg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvdb.materialchecklist.config.util

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.config.model.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.model.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.model.DragAndDropDismissKeyboardBehavior
import com.dvdb.materialchecklist.config.model.DragAndDropToggleBehavior
import com.dvdb.materialchecklist.util.MaterialChecklistUtil.assertOneSet
import com.dvdb.materialchecklist.util.getColorCompat

/**
 * Set the base text color of the checklist items.
 *
 * @param textColor The literal text color to use.
 * @param textColorRes The text color resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    assertOneSet("text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textColor != newTextColor) {
            config.textColor = newTextColor
        }
    }
    return this
}

/**
 * Set the link text color of the checklist items.
 *
 * @param textColor The literal text color to use.
 * @param textColorRes The text color resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setLinkTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    assertOneSet("link text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textLinkTextColor != newTextColor) {
            config.textLinkTextColor = newTextColor
        }
    }
    return this
}

/**
 * Set the text size of the checklist items.
 *
 * @param textSize The literal text size to use.
 * @param textSizeRes The text size resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setTextSize(
    textSize: Float? = null,
    @DimenRes textSizeRes: Int? = null
): MaterialChecklist {
    assertOneSet("text size", textSize, textSizeRes)
    val context = context
    if (context != null) {
        val newTextSize = textSize ?: context.resources.getDimension(textSizeRes!!)
        if (config.textSize != newTextSize) {
            config.textSize = newTextSize
        }
    }
    return this
}

/**
 * Set the text of the create new checklist item.
 *
 * @param text The literal text to use.
 * @param textRes The text resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setNewItemText(
    text: String? = null,
    @StringRes textRes: Int? = null
): MaterialChecklist {
    assertOneSet("new item text", text, textRes)
    val context = context
    if (context != null) {
        val newText = text ?: context.getString(textRes!!)
        if (config.textNewItem != newText) {
            config.textNewItem = newText
        }
    }
    return this
}

/**
 * Set the text alpha of the checklist items.
 *
 * @param alpha The literal text alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setCheckedItemTextAlpha(alpha: Float): MaterialChecklist {
    if (config.textAlphaCheckedItem != alpha) {
        config.textAlphaCheckedItem = alpha
    }
    return this
}

/**
 * Set the text alpha of the create new checklist item.
 *
 * @param alpha The literal text alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setNewItemTextAlpha(alpha: Float): MaterialChecklist {
    if (config.textAlphaNewItem != alpha) {
        config.textAlphaNewItem = alpha
    }
    return this
}

/**
 * Set the text typeface of the checklist items.
 *
 * @param typeface The typeface to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setTextTypeFace(typeface: Typeface): MaterialChecklist {
    if (config.textTypeFace != typeface) {
        config.textTypeFace = typeface
    }
    return this
}

/**
 * Set whether the links in the text of the checklist items should be clickable.
 *
 * @param linksClickable The clickable links flag to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setLinksClickable(linksClickable: Boolean): MaterialChecklist {
    if (config.textLinksClickable != linksClickable) {
        config.textLinksClickable = linksClickable
    }
    return this
}

/**
 * Set the base icon tint color of the checklist items.
 *
 * This tint color will be applied to the various checklist
 * icons namely the drag indicator, delete and add icon.
 *
 * @param tintColor The literal tint color to use.
 * @param tintColorRes The tint color resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setIconTintColor(
    @ColorInt tintColor: Int? = null,
    @ColorRes tintColorRes: Int? = null
): MaterialChecklist {
    assertOneSet("icon tint color", tintColor, tintColorRes)
    val context = context
    if (context != null) {
        val newTintColor = tintColor ?: context.getColorCompat(tintColorRes!!)
        if (config.iconTintColor != newTintColor) {
            config.iconTintColor = newTintColor
        }
    }
    return this
}

/**
 * Set the alpha of the drag indicator icon of the checklist items.
 *
 * @param alpha The literal icon alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setDragIndicatorIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaDragIndicator != alpha) {
        config.iconAlphaDragIndicator = alpha
    }
    return this
}

/**
 * Set the alpha of the delete icon of the checklist items.
 *
 * @param alpha The literal icon alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setDeleteIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaDelete != alpha) {
        config.iconAlphaDelete = alpha
    }
    return this
}

/**
 * Set the alpha of the add icon of the checklist items.
 *
 * @param alpha The literal icon alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setAddIconAlpha(alpha: Float): MaterialChecklist {
    if (config.iconAlphaAdd != alpha) {
        config.iconAlphaAdd = alpha
    }
    return this
}

/**
 * Set the checkbox tint color of the checklist items.
 *
 * @param tintColor The literal tint color to use.
 * @param tintColorRes The tint color resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setCheckboxTintColor(
    @ColorInt tintColor: Int? = null,
    @ColorRes tintColorRes: Int? = null
): MaterialChecklist {
    assertOneSet("checkbox tint color", tintColor, tintColorRes)
    if (context != null) {
        val newTintColor = tintColor ?: context.getColorCompat(tintColorRes!!)
        if (config.checkboxTintColor != newTintColor) {
            config.checkboxTintColor = newTintColor
        }
    }
    return this
}

/**
 * Set the alpha of the checkbox of the checked checklist items.
 *
 * @param alpha The literal alpha to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setCheckedItemCheckboxAlpha(alpha: Float): MaterialChecklist {
    if (config.checkboxAlphaCheckedItem != alpha) {
        config.checkboxAlphaCheckedItem = alpha
    }
    return this
}

/**
 * Set the drag-and-drop toggle behavior of the checklist items.
 *
 * @param behavior The drag-and-drop toggle behavior to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setDragAndDropToggleBehavior(behavior: DragAndDropToggleBehavior): MaterialChecklist {
    if (config.dragAndDropToggleBehavior != behavior) {
        config.dragAndDropToggleBehavior = behavior
    }
    return this
}

/**
 * Set the drag-and-drop dismiss keyboard behavior of the checklist items.
 *
 * @param behavior The drag-and-drop dismiss keyboard behavior to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setDragAndDropDismissKeyboardBehavior(behavior: DragAndDropDismissKeyboardBehavior): MaterialChecklist {
    if (config.dragAndDropDismissKeyboardBehavior != behavior) {
        config.dragAndDropDismissKeyboardBehavior = behavior
    }
    return this
}

/**
 * Set the background color of the checklist item while it is being dragged.
 *
 * @param backgroundColor The literal background color to use.
 * @param backgroundColorRes The background color resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setDragAndDropItemActiveBackgroundColor(
    @ColorInt backgroundColor: Int? = null,
    @ColorRes backgroundColorRes: Int? = null
): MaterialChecklist {
    assertOneSet("drag and drop item active background color", backgroundColor, backgroundColorRes)
    val context = context
    if (context != null) {
        val newBackgroundColor = backgroundColor ?: context.getColorCompat(backgroundColorRes!!)
        if (config.dragAndDropActiveItemBackgroundColor != newBackgroundColor) {
            config.dragAndDropActiveItemBackgroundColor = newBackgroundColor
        }
    }
    return this
}

/**
 * Set the on item checked behavior of the checklist items.
 *
 * @param behavior The on item checked behavior to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setOnItemCheckedBehavior(behavior: BehaviorCheckedItem): MaterialChecklist {
    if (config.behaviorCheckedItem != behavior) {
        config.behaviorCheckedItem = behavior
    }
    return this
}

/**
 * Set the on item unchecked behavior of the checklist items.
 *
 * @param behavior The on item unchecked behavior to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setOnItemUncheckedBehavior(behavior: BehaviorUncheckedItem): MaterialChecklist {
    if (config.behaviorUncheckedItem != behavior) {
        config.behaviorUncheckedItem = behavior
    }
    return this
}

/**
 * Set the top padding of the first checklist item, in addition to the
 * top padding of [setItemTopAndBottomPadding].
 *
 * @param padding The literal padding to use.
 * @param paddingRes The padding resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setItemFirstTopPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    assertOneSet("item first top padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.itemFirstTopPadding != newPadding) {
            config.itemFirstTopPadding = newPadding
        }
    }
    return this
}

/**
 * Set the top and bottom padding of the checklist items.
 *
 * @param padding The literal padding to use.
 * @param paddingRes The padding resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setItemTopAndBottomPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    assertOneSet("item top and bottom padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.itemTopAndBottomPadding != newPadding) {
            config.itemTopAndBottomPadding = newPadding
        }
    }
    return this
}

/**
 * Set the left and right padding of the checklist items.
 *
 * @param padding The literal padding to use.
 * @param paddingRes The padding resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setItemLeftAndRightPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    assertOneSet("item left and right padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.itemLeftAndRightPadding != newPadding) {
            config.itemLeftAndRightPadding = newPadding
        }
    }
    return this
}

/**
 * Set the bottom padding of the last checklist item, in addition to the
 * bottom padding of [setItemTopAndBottomPadding].
 *
 * @param padding The literal padding to use.
 * @param paddingRes The padding resource to use.
 *
 * Note that this call must be terminated with [applyConfiguration]
 * to take effect.
 */
fun MaterialChecklist.setItemLastBottomPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    assertOneSet("item last bottom padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.itemLastBottomPadding != newPadding) {
            config.itemLastBottomPadding = newPadding
        }
    }
    return this
}

/**
 * Apply the Checklist configuration options.
 */
fun MaterialChecklist.applyConfiguration() {
    manager.setConfig(config.toManagerConfig())
}
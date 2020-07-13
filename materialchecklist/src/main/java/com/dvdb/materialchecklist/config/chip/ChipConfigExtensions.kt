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

package com.dvdb.materialchecklist.config.chip

import androidx.annotation.*
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.util.MaterialChecklistUtil
import com.dvdb.materialchecklist.util.getColorCompat

fun MaterialChecklist.setChipBackgroundColor(
    @ColorInt backgroundColor: Int? = null,
    @ColorRes backgroundColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip background color", backgroundColor, backgroundColorRes)
    val context = context
    if (context != null) {
        val newBackgroundColor = backgroundColor ?: context.getColorCompat(backgroundColorRes!!)
        if (config.chipConfig.backgroundColor != newBackgroundColor) {
            config.chipConfig.backgroundColor = newBackgroundColor
        }
    }
    return this
}

fun MaterialChecklist.setChipStrokeColor(
    @ColorInt strokeColor: Int? = null,
    @ColorRes strokeColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip stroke color", strokeColor, strokeColorRes)
    val context = context
    if (context != null) {
        val newStrokeColor = strokeColor ?: context.getColorCompat(strokeColorRes!!)
        if (config.chipConfig.strokeColor != newStrokeColor) {
            config.chipConfig.strokeColor = newStrokeColor
        }
    }
    return this
}

fun MaterialChecklist.setChipStrokeWidth(
    @Px width: Float? = null,
    @DimenRes widthRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip stroke width", width, widthRes)
    val context = context
    if (context != null) {
        val newWidth = width ?: context.resources.getDimension(widthRes!!)
        if (config.chipConfig.strokeWidth != newWidth) {
            config.chipConfig.strokeWidth = newWidth
        }
    }
    return this
}

fun MaterialChecklist.setChipIconSize(
    @Dimension iconSize: Float? = null,
    @DimenRes iconSizeRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip icon size", iconSize, iconSizeRes)
    val context = context
    if (context != null) {
        val newIconSize = iconSize ?: context.resources.getDimension(iconSizeRes!!)
        if (config.chipConfig.iconSize != newIconSize) {
            config.chipConfig.iconSize = newIconSize
        }
    }
    return this
}

fun MaterialChecklist.setChipIconEndPadding(
    @Dimension padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip icon end padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.chipConfig.iconEndPadding != newPadding) {
            config.chipConfig.iconEndPadding = newPadding
        }
    }
    return this
}

fun MaterialChecklist.setChipMinHeight(
    @Dimension minHeight: Float? = null,
    @DimenRes minHeightRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip min height", minHeight, minHeightRes)
    val context = context
    if (context != null) {
        val newMinHeight = minHeight ?: context.resources.getDimension(minHeightRes!!)
        if (config.chipConfig.minHeight != newMinHeight) {
            config.chipConfig.minHeight = newMinHeight
        }
    }
    return this
}

fun MaterialChecklist.setChipHorizontalSpacing(
    @Px spacing: Int? = null,
    @DimenRes spacingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip horizontal spacing", spacing, spacingRes)
    val context = context
    if (context != null) {
        val newSpacing = spacing ?: context.resources.getDimensionPixelOffset(spacingRes!!)
        if (config.chipConfig.horizontalSpacing != newSpacing) {
            config.chipConfig.horizontalSpacing = newSpacing
        }
    }
    return this
}

fun MaterialChecklist.setChipInternalLeftAndRightPadding(
    @Px padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("chip internal left and right padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.chipConfig.leftAndRightInternalPadding != newPadding) {
            config.chipConfig.leftAndRightInternalPadding = newPadding
        }
    }
    return this
}
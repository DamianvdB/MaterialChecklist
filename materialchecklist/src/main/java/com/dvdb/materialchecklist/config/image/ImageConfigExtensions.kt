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

package com.dvdb.materialchecklist.config.image

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.Px
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.util.MaterialChecklistUtil
import com.dvdb.materialchecklist.util.getColorCompat

fun MaterialChecklist.setImageMaxColumnSpan(columnCount: Int): MaterialChecklist {
    if (config.imageConfig.maxColumnSpan != columnCount) {
        config.imageConfig.maxColumnSpan = columnCount
    }
    return this
}

fun MaterialChecklist.setImageTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.imageConfig.textColor != newTextColor) {
            config.imageConfig.textColor = newTextColor
        }
    }
    return this
}

fun MaterialChecklist.setImageStrokeColor(
    @ColorInt strokeColor: Int? = null,
    @ColorRes strokeColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image stroke color", strokeColor, strokeColorRes)
    val context = context
    if (context != null) {
        val newStrokeColor = strokeColor ?: context.getColorCompat(strokeColorRes!!)
        if (config.imageConfig.strokeColor != newStrokeColor) {
            config.imageConfig.strokeColor = newStrokeColor
        }
    }
    return this
}

fun MaterialChecklist.setImageStrokeWidth(
    @Px strokeWidth: Int? = null,
    @DimenRes strokeWidthRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image stroke width", strokeWidth, strokeWidthRes)
    val context = context
    if (context != null) {
        val newStrokeWidth =
            strokeWidth ?: context.resources.getDimensionPixelSize(strokeWidthRes!!)
        if (config.imageConfig.strokeWidth != newStrokeWidth) {
            config.imageConfig.strokeWidth = newStrokeWidth
        }
    }
    return this
}

fun MaterialChecklist.setImageTextSize(
    textSize: Float? = null,
    @DimenRes textSizeRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image text size", textSize, textSizeRes)
    val context = context
    if (context != null) {
        val newTextSize = textSize ?: context.resources.getDimension(textSizeRes!!)
        if (config.imageConfig.textSize != newTextSize) {
            config.imageConfig.textSize = newTextSize
        }
    }
    return this
}

fun MaterialChecklist.setImageCornerRadius(
    radius: Float? = null,
    @DimenRes radiusRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image corner radius", radius, radiusRes)
    val context = context
    if (context != null) {
        val newRadius = radius ?: context.resources.getDimension(radiusRes!!)
        if (config.imageConfig.cornerRadius != newRadius) {
            config.imageConfig.cornerRadius = newRadius
        }
    }
    return this
}

fun MaterialChecklist.setImageInnerPadding(
    @Px padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image inner padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.imageConfig.innerPadding != newPadding) {
            config.imageConfig.innerPadding = newPadding
        }
    }
    return this
}

fun MaterialChecklist.setImageLeftAndRightPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image left and right padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.imageConfig.leftAndRightPadding != newPadding) {
            config.imageConfig.leftAndRightPadding = newPadding
        }
    }
    return this
}

fun MaterialChecklist.setImageTopAndBottomPadding(
    padding: Float? = null,
    @DimenRes paddingRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("image top and bottom padding", padding, paddingRes)
    val context = context
    if (context != null) {
        val newPadding = padding ?: context.resources.getDimension(paddingRes!!)
        if (config.imageConfig.topAndBottomPadding != newPadding) {
            config.imageConfig.topAndBottomPadding = newPadding
        }
    }
    return this
}

fun MaterialChecklist.setImageAdjustItemTextSize(adjustTextSize: Boolean): MaterialChecklist {
    if (config.imageConfig.adjustItemTextSize != adjustTextSize) {
        config.imageConfig.adjustItemTextSize = adjustTextSize
    }
    return this
}
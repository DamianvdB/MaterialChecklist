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

package com.dvdb.materialchecklist.config

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.util.MaterialChecklistUtil
import com.dvdb.materialchecklist.util.getColorCompat

fun MaterialChecklist.setTitleHint(
    text: String? = null,
    @StringRes textRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("title hint", text, textRes)
    val context = context
    if (context != null) {
        val newText = text ?: context.getString(textRes!!)
        if (config.textTitleHint != newText) {
            config.textTitleHint = newText
            manager.setTitleConfig(config.totTitleManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setTitleTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("title text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textTitleColor != newTextColor) {
            config.textTitleColor = newTextColor
            manager.setTitleConfig(config.totTitleManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setTitleLinkTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("title link text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textTitleLinkColor != newTextColor) {
            config.textTitleLinkColor = newTextColor
            manager.setTitleConfig(config.totTitleManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setTitleHintTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("title hint text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textTitleHintColor != newTextColor) {
            config.textTitleHintColor = newTextColor
            manager.setTitleConfig(config.totTitleManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setTitleClickableLinks(clickableLinks: Boolean): MaterialChecklist {
    if (config.textTitleClickableLinks != clickableLinks) {
        config.textTitleClickableLinks = clickableLinks
        manager.setTitleConfig(config.totTitleManagerConfig())
    }
    return this
}

fun MaterialChecklist.setTitleEditable(editable: Boolean): MaterialChecklist {
    if (config.textTitleEditable != editable) {
        config.textTitleEditable = editable
        manager.setTitleConfig(config.totTitleManagerConfig())
    }
    return this
}

fun MaterialChecklist.setTitleShowActionIcon(showActionIcon: Boolean): MaterialChecklist {
    if (config.iconTitleShowAction != showActionIcon) {
        config.iconTitleShowAction = showActionIcon
        manager.setTitleConfig(config.totTitleManagerConfig())
    }
    return this
}

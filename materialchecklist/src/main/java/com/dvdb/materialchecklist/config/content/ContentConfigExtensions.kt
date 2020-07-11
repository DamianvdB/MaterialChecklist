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

package com.dvdb.materialchecklist.config.content

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.util.MaterialChecklistUtil
import com.dvdb.materialchecklist.util.getColorCompat

fun MaterialChecklist.setContentHint(
    text: String? = null,
    @StringRes textRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("content hint", text, textRes)
    val context = context
    if (context != null) {
        val newText = text ?: context.getString(textRes!!)
        if (config.textContentHint != newText) {
            config.textContentHint = newText
            manager.setContentConfig(config.toContentManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setContentLinkTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("content link text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textContentLinkColor != newTextColor) {
            config.textContentLinkColor = newTextColor
            manager.setContentConfig(config.toContentManagerConfig())
        }
    }
    return this
}

fun MaterialChecklist.setContentHintTextColor(
    @ColorInt textColor: Int? = null,
    @ColorRes textColorRes: Int? = null
): MaterialChecklist {
    MaterialChecklistUtil.assertOneSet("content hint text color", textColor, textColorRes)
    val context = context
    if (context != null) {
        val newTextColor = textColor ?: context.getColorCompat(textColorRes!!)
        if (config.textContentHintColor != newTextColor) {
            config.textContentHintColor = newTextColor
            manager.setContentConfig(config.toContentManagerConfig())
        }
    }
    return this
}
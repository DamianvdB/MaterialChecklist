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

package com.dvdb.checklist.sample

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.dvdb.checklist.R
import com.dvdb.materialchecklist.config.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.DragAndDropToggleMode

internal class ChecklistConfiguration(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    /**
     * Text
     */
    val textColor: Int
        get() = parseColorOrNull(sharedPreferences.getString(context.getString(R.string.pref_settings_text_color), null) ?: "")
            ?: ContextCompat.getColor(context, com.dvdb.materialchecklist.R.color.mc_text_checklist_item_text)

    val textSize: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_text_size), null))?.toFloatOrNull()?.toPx(context)
            ?: context.resources.getDimension(com.dvdb.materialchecklist.R.dimen.mc_item_checklist_text_size)

    val textNewItem: String
        get() = sharedPreferences.getString(context.getString(R.string.pref_settings_text_new_item), null)
            ?: context.getString(com.dvdb.materialchecklist.R.string.mc_item_checklist_new_text)

    val textAlphaCheckedItem: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_text_alpha_checked_item), null))?.toFloatOrNull()
            ?: 0.4F

    val textAlphaNewItem: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_text_alpha_new_item), null))?.toFloatOrNull()
            ?: 0.5F

    val textTypeFace: Typeface?
        get() {
            val values = context.resources.getStringArray(R.array.settings_text_type_face_values)
            return when (sharedPreferences.getString(context.getString(R.string.pref_settings_text_type_face), null)) {
                values[0] -> Typeface.DEFAULT
                values[1] -> Typeface.MONOSPACE
                values[2] -> Typeface.SANS_SERIF
                values[3] -> Typeface.SERIF
                else -> null
            }
        }

    /**
     * Icon
     */
    val iconTintColor: Int
        get() = parseColorOrNull(sharedPreferences.getString(context.getString(R.string.pref_settings_icon_tint_color), null) ?: "")
            ?: ContextCompat.getColor(context, com.dvdb.materialchecklist.R.color.mc_icon_tint)

    val iconAlphaDragIndicator: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_icon_alpha_drag_indicator), null))?.toFloatOrNull()
            ?: 0.5F

    val iconAlphaDelete: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_icon_alpha_delete), null))?.toFloatOrNull()
            ?: 0.9F

    val iconAlphaAdd: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_icon_alpha_add), null))?.toFloatOrNull()
            ?: 0.7F

    /**
     * Checkbox
     */
    val checkboxTintColor: Int?
        get() = parseColorOrNull(sharedPreferences.getString(context.getString(R.string.pref_settings_checkbox_tint_color), null) ?: "")

    val checkboxAlphaCheckedItem: Float
        get() = (sharedPreferences.getString(context.getString(R.string.pref_settings_checkbox_alpha_checked_item), null))?.toFloatOrNull()
            ?: 0.4F

    /**
     * Drag-and-drop
     */
    val dragAndDropToggleMode: DragAndDropToggleMode
        get() = DragAndDropToggleMode.fromInt(
            sharedPreferences.getString(context.getString(R.string.pref_settings_drag_and_drop_toggle_mode), null)?.toIntOrNull()
                ?: 0
        )

    val dragAndDropActiveItemBackgroundColor: Int?
        get() = parseColorOrNull(
            sharedPreferences.getString(context.getString(R.string.pref_settings_drag_and_drop_item_background_color), null)
                ?: ""
        )

    /**
     *  Behavior
     */
    val behaviorCheckedItem: BehaviorCheckedItem
        get() = BehaviorCheckedItem.fromInt(
            sharedPreferences.getString(context.getString(R.string.pref_settings_behavior_checked_item), null)?.toIntOrNull()
                ?: 0
        )

    val behaviorUncheckedItem: BehaviorUncheckedItem
        get() = BehaviorUncheckedItem.fromInt(
            sharedPreferences.getString(context.getString(R.string.pref_settings_behavior_unchecked_item), null)?.toIntOrNull()
                ?: 0
        )

    /**
     * Item
     */
    val itemHorizontalPadding: Float?
        get() = sharedPreferences.getString(context.getString(R.string.pref_settings_item_horizontal_padding), null)?.toFloatOrNull()?.toPx(context)

    private fun parseColorOrNull(color: String): Int? {
        return try {
            Color.parseColor(color)
        } catch (e: Exception) {
            null
        }
    }

    private fun Float.toPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }
}
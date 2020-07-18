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

package com.dvdb.checklist.sample.config

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.dvdb.checklist.R
import com.dvdb.materialchecklist.config.checklist.model.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.checklist.model.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.checklist.model.DragAndDropDismissKeyboardBehavior
import com.dvdb.materialchecklist.config.checklist.model.DragAndDropToggleBehavior

internal class ChecklistConfiguration(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    /**
     * Text
     */
    val textColor: Int
        get() = parseColorOrNull(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_text_color),
                null
            ) ?: ""
        ) ?: ContextCompat.getColor(
            context,
            com.dvdb.materialchecklist.R.color.mc_text_checklist_item_text
        )

    val textSize: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_text_size),
            null
        ))?.toFloatOrNull()?.toPx(context)
            ?: context.resources.getDimension(com.dvdb.materialchecklist.R.dimen.mc_item_checklist_text_size)

    val textNewItem: String
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_settings_text_new_item),
            null
        ) ?: context.getString(com.dvdb.materialchecklist.R.string.mc_item_checklist_new_text)

    val textAlphaCheckedItem: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_text_alpha_checked_item),
            null
        ))?.toFloatOrNull()
            ?: 0.4F

    val textAlphaNewItem: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_text_alpha_new_item),
            null
        ))?.toFloatOrNull()
            ?: 0.5F

    val textTypeFace: Typeface?
        get() {
            val values = context.resources.getStringArray(R.array.settings_text_type_face_values)
            return when (sharedPreferences.getString(
                context.getString(R.string.pref_settings_text_type_face),
                "default"
            )) {
                values[0] -> Typeface.DEFAULT
                values[1] -> Typeface.MONOSPACE
                values[2] -> Typeface.SANS_SERIF
                values[3] -> Typeface.SERIF
                else -> null
            }
        }

    val textTitleHint: String = "Title"
    val textTitleColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    val textTitleLinkColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    val textTitleHintColor: Int = Color.parseColor("#9575cd")
    val textTitleClickableLinks: Boolean = true

    val textContentHint: String = "Content"
    val textContentLinkColor: Int = textTitleLinkColor
    val textContentHintColor: Int = Color.parseColor("#757575")
    val textContentClickableLinks: Boolean = true

    val textEditable: Boolean = true

    /**
     * Icon
     */
    val iconTintColor: Int
        get() = parseColorOrNull(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_icon_tint_color),
                null
            ) ?: ""
        ) ?: ContextCompat.getColor(
            context,
            com.dvdb.materialchecklist.R.color.mc_icon_tint
        )

    val iconAlphaDragIndicator: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_icon_alpha_drag_indicator),
            null
        ))?.toFloatOrNull()
            ?: 0.5F

    val iconAlphaDelete: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_icon_alpha_delete),
            null
        ))?.toFloatOrNull()
            ?: 0.9F

    val iconAlphaAdd: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_icon_alpha_add),
            null
        ))?.toFloatOrNull()
            ?: 0.7F

    val iconTitleShowAction: Boolean = true

    /**
     * Chip
     */
    val chipBackgroundColor: Int? = Color.TRANSPARENT
    val chipStrokeColor: Int? = Color.parseColor("#bdbdbd")
    val chipStrokeWidth: Float? = 1f.toPx(context)
    val chipIconSize: Float =
        context.resources.getDimension(com.dvdb.materialchecklist.R.dimen.mc_icon_size_small)
    val chipIconEndPadding: Float? = null
    val chipMinHeight: Float? =
        context.resources.getDimension(com.dvdb.materialchecklist.R.dimen.mc_item_chip_min_height)
    val chipHorizontalSpacing: Int =
        context.resources.getDimensionPixelSize(com.dvdb.materialchecklist.R.dimen.mc_spacing_medium_large)
    val chipLeftAndRightInternalPadding: Float =
        context.resources.getDimension(com.dvdb.materialchecklist.R.dimen.mc_spacing_medium)

    /**
     * Image
     */
    val imageMaxColumnSpan: Int =
        context.resources.getInteger(R.integer.mc_item_image_container_column_span)
    val imageTextColor: Int? = null
    val imageStrokeColor: Int = ContextCompat.getColor(context, R.color.mc_image_container_stroke)
    val imageStrokeWidth: Int =
        context.resources.getDimensionPixelSize(R.dimen.mc_item_image_stroke_width)
    val imageTextSize: Float = context.resources.getDimension(R.dimen.mc_item_image_text_size)
    val imageCornerRadius: Float =
        context.resources.getDimension(R.dimen.mc_item_image_corner_radius)
    val imageInnerPadding: Float =
        context.resources.getDimension(R.dimen.mc_item_image_inner_margin)
    val imageLeftAndRightPadding: Float? = null
    val imageTopAndBottomPadding: Float? = null
    val imageAdjustItemTextSize: Boolean = true

    /**
     * Checkbox
     */
    val checkboxTintColor: Int?
        get() = parseColorOrNull(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_checkbox_tint_color),
                null
            ) ?: ""
        )

    val checkboxAlphaCheckedItem: Float
        get() = (sharedPreferences.getString(
            context.getString(R.string.pref_settings_checkbox_alpha_checked_item),
            null
        ))?.toFloatOrNull()
            ?: 0.4F

    /**
     * Drag-and-drop
     */
    val dragAndDropToggleBehavior: DragAndDropToggleBehavior
        get() = DragAndDropToggleBehavior.fromString(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_drag_and_drop_toggle_behavior),
                null
            ) ?: ""
        )

    val dragAndDismissKeyboardBehavior: DragAndDropDismissKeyboardBehavior
        get() = DragAndDropDismissKeyboardBehavior.fromString(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_drag_and_drop_dismiss_keyboard_behavior),
                null
            ) ?: ""
        )

    val dragAndDropActiveItemBackgroundColor: Int?
        get() = parseColorOrNull(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_drag_and_drop_item_background_color),
                "#FFFFFF"
            ) ?: ""
        )

    /**
     *  Behavior
     */
    val behaviorCheckedItem: BehaviorCheckedItem
        get() = BehaviorCheckedItem.fromString(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_behavior_checked_item),
                null
            ) ?: ""
        )

    val behaviorUncheckedItem: BehaviorUncheckedItem
        get() = BehaviorUncheckedItem.fromString(
            sharedPreferences.getString(
                context.getString(R.string.pref_settings_behavior_unchecked_item),
                null
            ) ?: ""
        )

    /**
     * Item
     */
    val itemFirstTopPadding: Float?
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_settings_item_first_top_padding),
            "16.0"
        )?.toFloatOrNull()
            ?.toPx(context)

    val itemLeftAndRightPadding: Float?
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_settings_item_left_and_right_padding),
            "8.0"
        )?.toFloatOrNull()
            ?.toPx(context)

    val itemTopAndBottomPadding: Float?
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_settings_item_top_and_bottom_padding),
            "2.0"
        )?.toFloatOrNull()
            ?.toPx(context)

    val itemLastBottomPadding: Float?
        get() = sharedPreferences.getString(
            context.getString(R.string.pref_settings_item_last_bottom_padding),
            "16.0"
        )?.toFloatOrNull()
            ?.toPx(context)

    private fun parseColorOrNull(color: String): Int? {
        return try {
            Color.parseColor(color)
        } catch (e: Exception) {
            null
        }
    }

    private fun Float.toPx(context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this,
            context.resources.displayMetrics
        )
    }
}
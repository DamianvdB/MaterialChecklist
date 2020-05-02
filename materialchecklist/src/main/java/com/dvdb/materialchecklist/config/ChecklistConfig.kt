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

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleableRes
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.manager.config.ChecklistManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig
import com.dvdb.materialchecklist.util.getColorCompat

internal class ChecklistConfig(
    context: Context,
    attrs: AttributeSet?,

    /**
     * Text
     */
    @ColorInt var textColor: Int = context.getColorCompat(R.color.mc_text_checklist_item_text),
    @Px var textSize: Float = context.resources.getDimension(R.dimen.mc_item_checklist_text_size),
    var textNewItem: String = context.getString(R.string.mc_item_checklist_new_text),
    var textAlphaCheckedItem: Float = 0.4F,
    var textAlphaNewItem: Float = 0.5F,
    var textTypeFace: Typeface? = null,

    /**
     * Icon
     */
    @ColorInt var iconTintColor: Int = context.getColorCompat(R.color.mc_icon_tint),
    var iconAlphaDragIndicator: Float = 0.5F,
    var iconAlphaDelete: Float = 0.9F,
    var iconAlphaAdd: Float = 0.7F,

    /**
     * Checkbox
     */
    @ColorInt var checkboxTintColor: Int? = null,
    var checkboxAlphaCheckedItem: Float = 0.4F,

    /**
     * Drag-and-drop
     */
    var dragAndDropToggleMode: DragAndDropToggleMode = DragAndDropToggleMode.ON_TOUCH,
    @ColorInt var dragAndDropActiveItemBackgroundColor: Int? = null,

    /**
     *  Behavior
     */
    var behaviorCheckedItem: BehaviorCheckedItem = BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS,
    var behaviorUncheckedItem: BehaviorUncheckedItem = BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION,

    /**
     * Item
     */
    @Px var itemFirstTopPadding: Float? = null,
    @Px var itemLeftAndRightPadding: Float? = null,
    @Px var itemLastBottomPadding: Float? = null
) : Config {

    init {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialChecklist)

        try {
            initTextAttributes(attributes)
            initIconAttributes(attributes)
            initCheckboxAttributes(attributes)
            initDragAndDropAttributes(attributes)
            initBehaviorAttributes(attributes)
            initItemAttributes(attributes)
        } finally {
            attributes.recycle()
        }
    }

    @CheckResult
    fun toManagerConfig() = ChecklistManagerConfig(
        dragAndDropEnabled = dragAndDropToggleMode != DragAndDropToggleMode.NONE,
        behaviorCheckedItem = behaviorCheckedItem,
        behaviorUncheckedItem = behaviorUncheckedItem,
        itemFirstTopPadding = itemFirstTopPadding,
        itemLastBottomPadding = itemLastBottomPadding,
        adapterConfig = toAdapterConfig()
    )

    @CheckResult
    fun toAdapterConfig() = ChecklistItemAdapterConfig(
        checklistConfig = ChecklistRecyclerHolderConfig(
            textColor = textColor,
            textSize = textSize,
            textAlphaCheckedItem = textAlphaCheckedItem,
            textTypeFace = textTypeFace,
            iconTintColor = iconTintColor,
            iconAlphaDragIndicator = iconAlphaDragIndicator,
            iconAlphaDelete = iconAlphaDelete,
            checkboxAlphaCheckedItem = checkboxAlphaCheckedItem,
            checkboxTintColor = checkboxTintColor,
            dragAndDropToggleMode = dragAndDropToggleMode,
            dragAndDropActiveBackgroundColor = dragAndDropActiveItemBackgroundColor,
            leftAndRightPadding = itemLeftAndRightPadding
        ),
        checklistNewConfig = ChecklistNewRecyclerHolderConfig(
            text = textNewItem,
            textColor = textColor,
            textSize = textSize,
            textAlpha = textAlphaNewItem,
            textTypeFace = textTypeFace,
            iconTintColor = iconTintColor,
            iconAlphaAdd = iconAlphaAdd,
            leftAndRightPadding = itemLeftAndRightPadding
        )
    )

    private fun initTextAttributes(attributes: TypedArray) {
        textColor = attributes.getColor(
            R.styleable.MaterialChecklist_text_color,
            textColor
        )

        textSize = attributes.getDimension(
            R.styleable.MaterialChecklist_text_size,
            textSize
        )

        textNewItem = attributes.getString(
            R.styleable.MaterialChecklist_text_new_item
        ) ?: textNewItem

        textAlphaCheckedItem = attributes.getFloat(
            R.styleable.MaterialChecklist_text_alpha_checked_item,
            textAlphaCheckedItem
        )

        textAlphaNewItem = attributes.getFloat(
            R.styleable.MaterialChecklist_text_alpha_new_item,
            textAlphaNewItem
        )
    }

    private fun initIconAttributes(attributes: TypedArray) {
        iconTintColor = attributes.getColor(
            R.styleable.MaterialChecklist_icon_tint_color,
            iconTintColor
        )

        iconAlphaDragIndicator = attributes.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_drag_indicator,
            iconAlphaDragIndicator
        )

        iconAlphaDelete = attributes.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_delete,
            iconAlphaDelete
        )

        iconAlphaAdd = attributes.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_add,
            iconAlphaAdd
        )
    }

    private fun initCheckboxAttributes(attributes: TypedArray) {
        checkboxTintColor = attributes.getColor(
            R.styleable.MaterialChecklist_checkbox_tint_color,
            0
        ).run { if (this == 0) checkboxTintColor else this }

        checkboxAlphaCheckedItem = attributes.getFloat(
            R.styleable.MaterialChecklist_checkbox_alpha_checked_item,
            checkboxAlphaCheckedItem
        )
    }

    private fun initDragAndDropAttributes(attributes: TypedArray) {
        dragAndDropToggleMode = DragAndDropToggleMode.values()[attributes.getInt(
            R.styleable.MaterialChecklist_drag_and_drop_toggle_mode,
            dragAndDropToggleMode.ordinal
        )]

        dragAndDropActiveItemBackgroundColor = attributes.getColor(
            R.styleable.MaterialChecklist_drag_and_drop_item_active_background_color,
            0
        ).run { if (this == 0) dragAndDropActiveItemBackgroundColor else this }
    }

    private fun initBehaviorAttributes(attributes: TypedArray) {
        behaviorCheckedItem = BehaviorCheckedItem.values()[attributes.getInt(
            R.styleable.MaterialChecklist_behavior_checked_item,
            behaviorCheckedItem.ordinal
        )]

        behaviorUncheckedItem = BehaviorUncheckedItem.values()[attributes.getInt(
            R.styleable.MaterialChecklist_behavior_unchecked_item,
            behaviorUncheckedItem.ordinal
        )]
    }

    private fun initItemAttributes(attributes: TypedArray) {
        attributes.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_first_top)?.let {
            itemFirstTopPadding = it
        }

        attributes.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_left_and_right)?.let {
            itemLeftAndRightPadding = it
        }

        attributes.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_last_bottom)?.let {
            itemLastBottomPadding = it
        }
    }

    @CheckResult
    private fun TypedArray.getDimensionOrNull(@StyleableRes index: Int): Float? {
        return getDimension(index, 0F).run { if (this != 0f) this else null }
    }
}
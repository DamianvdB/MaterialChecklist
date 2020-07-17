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

import ChecklistManagerConfig
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.config.checklist.model.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.checklist.model.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.checklist.model.DragAndDropDismissKeyboardBehavior
import com.dvdb.materialchecklist.config.checklist.model.DragAndDropToggleBehavior
import com.dvdb.materialchecklist.config.chip.model.ChipConfig
import com.dvdb.materialchecklist.manager.chip.model.ChipManagerConfig
import com.dvdb.materialchecklist.manager.content.model.ContentManagerConfig
import com.dvdb.materialchecklist.manager.title.model.TitleManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.checklistnew.model.ChecklistNewRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerHolderConfig
import com.dvdb.materialchecklist.util.getColorCompat
import com.dvdb.materialchecklist.util.getDrawableCompat

/**
 * Defines the configurable appearance and behavior of the checklist system.
 */
internal class ChecklistConfig(
    context: Context,
    attrs: AttributeSet?,

    /**
     * Text
     */
    @ColorInt var textColor: Int = context.getColorCompat(R.color.mc_text_checklist_item_text),
    @ColorInt var textTitleColor: Int? = null,
    @ColorInt var textTitleLinkColor: Int? = null,
    @ColorInt var textTitleHintColor: Int? = null,
    @ColorInt var textContentLinkColor: Int? = null,
    @ColorInt var textContentHintColor: Int? = null,
    var textSize: Float = context.resources.getDimension(R.dimen.mc_item_checklist_text_size),
    var textTitleHint: String = String(),
    var textContentHint: String = String(),
    var textNewItem: String = context.getString(R.string.mc_item_checklist_new_text),
    var textAlphaCheckedItem: Float = 0.4F,
    var textAlphaNewItem: Float = 0.5F,
    var textTitleClickableLinks: Boolean = true,
    var textContentClickableLinks: Boolean = true,
    var textEditable: Boolean = true,
    var textTypeFace: Typeface? = null,

    /**
     * Icon
     */
    @ColorInt var iconTintColor: Int = context.getColorCompat(R.color.mc_icon_tint),
    private val iconTitleAction: Drawable? = context.getDrawableCompat(R.drawable.ic_more_vert_white),
    var iconTitleShowAction: Boolean = false,
    private val iconDragIndicator: Drawable? = context.getDrawableCompat(R.drawable.ic_drag_indicator),
    var iconAlphaDragIndicator: Float = 0.5F,
    private val iconDelete: Drawable? = context.getDrawableCompat(R.drawable.ic_close),
    var iconAlphaDelete: Float = 0.9F,
    private val iconAdd: Drawable? = context.getDrawableCompat(R.drawable.ic_add),
    var iconAlphaAdd: Float = 0.7F,

    /**
     * Checkbox
     */
    @ColorInt var checkboxTintColor: Int? = null,
    var checkboxAlphaCheckedItem: Float = 0.4F,

    /**
     * Drag-and-drop
     */
    var dragAndDropToggleBehavior: DragAndDropToggleBehavior = DragAndDropToggleBehavior.DEFAULT,
    var dragAndDropDismissKeyboardBehavior: DragAndDropDismissKeyboardBehavior = DragAndDropDismissKeyboardBehavior.DEFAULT,
    @ColorInt var dragAndDropActiveItemBackgroundColor: Int? = null,

    /**
     *  Behavior
     */
    var behaviorCheckedItem: BehaviorCheckedItem = BehaviorCheckedItem.DEFAULT,
    var behaviorUncheckedItem: BehaviorUncheckedItem = BehaviorUncheckedItem.DEFAULT,

    /**
     * Item
     */
    var itemFirstTopPadding: Float? = null,
    var itemLeftAndRightPadding: Float? = null,
    var itemTopAndBottomPadding: Float = context.resources.getDimension(R.dimen.mc_item_checklist_top_bottom_padding),
    var itemLastBottomPadding: Float? = null
) : Config {

    private val titleTextSizeOffset: Float =
        context.resources.getDimension(R.dimen.mc_item_title_text_size_offset)

    private val chipTextSizeOffset: Float =
        context.resources.getDimension(R.dimen.mc_item_chip_text_size_offset)

    private val leftAndRightPaddingOffset: Float =
        context.resources.getDimension(R.dimen.mc_spacing_medium)

    private val chipTopAndBottomPaddingOffset: Float =
        context.resources.getDimension(R.dimen.mc_spacing_medium)

    val chipConfig: ChipConfig = ChipConfig(
        context,
        textColor = { textColor },
        textSize = { textSize - chipTextSizeOffset },
        typeFace = { textTypeFace },
        iconTintColor = { iconTintColor },
        leftAndRightPadding = { (itemLeftAndRightPadding ?: 0f) + leftAndRightPaddingOffset },
        topAndBottomPadding = { itemTopAndBottomPadding + chipTopAndBottomPaddingOffset }
    )

    init {
        val attributes: TypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MaterialChecklist
        )

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
    fun totTitleManagerConfig() = TitleManagerConfig(
        adapterConfig = toAdapterConfig()
    )

    @CheckResult
    fun toContentManagerConfig() = ContentManagerConfig(
        adapterConfig = toAdapterConfig()
    )

    @CheckResult
    fun toManagerConfig() = ChecklistManagerConfig(
        dragAndDropEnabled = dragAndDropToggleBehavior != DragAndDropToggleBehavior.NONE,
        dragAndDropDismissKeyboardBehavior = dragAndDropDismissKeyboardBehavior,
        behaviorCheckedItem = behaviorCheckedItem,
        behaviorUncheckedItem = behaviorUncheckedItem,
        itemFirstTopPadding = itemFirstTopPadding,
        itemLastBottomPadding = itemLastBottomPadding,
        adapterConfig = toAdapterConfig()
    )

    @CheckResult
    fun toChipManagerConfig() = ChipManagerConfig(
        adapterConfig = toAdapterConfig()
    )

    @CheckResult
    fun toAdapterConfig() = ChecklistItemAdapterConfig(
        titleConfig = TitleRecyclerHolderConfig(
            hint = textTitleHint,
            textColor = textTitleColor ?: textColor,
            linkTextColor = textTitleLinkColor ?: textColor,
            hintTextColor = textTitleHintColor,
            iconTintColor = iconTintColor,
            textSize = textSize + titleTextSizeOffset,
            isLinksClickable = textTitleClickableLinks,
            isEditable = textEditable,
            isShowActionIcon = iconTitleShowAction,
            typeFace = textTypeFace,
            typeFaceStyle = Typeface.BOLD,
            actionIcon = iconTitleAction,
            leftPadding = (itemLeftAndRightPadding ?: 0f).plus(leftAndRightPaddingOffset),
            rightPadding = itemLeftAndRightPadding
        ),
        contentConfig = ContentRecyclerHolderConfig(
            hint = textContentHint,
            textColor = textColor,
            linkTextColor = textContentLinkColor ?: textColor,
            hintTextColor = textContentHintColor,
            textSize = textSize,
            isLinksClickable = textContentClickableLinks,
            isEditable = textEditable,
            typeFace = textTypeFace,
            leftAndRightPadding = (itemLeftAndRightPadding ?: 0f) + (leftAndRightPaddingOffset)
        ),
        checklistConfig = ChecklistRecyclerHolderConfig(
            textColor = textColor,
            textSize = textSize,
            textAlphaCheckedItem = textAlphaCheckedItem,
            textTypeFace = textTypeFace,
            iconTintColor = iconTintColor,
            iconDragIndicator = iconDragIndicator,
            iconAlphaDragIndicator = iconAlphaDragIndicator,
            iconDelete = iconDelete,
            iconAlphaDelete = iconAlphaDelete,
            checkboxAlphaCheckedItem = checkboxAlphaCheckedItem,
            checkboxTintColor = checkboxTintColor,
            dragAndDropToggleBehavior = dragAndDropToggleBehavior,
            dragAndDropActiveBackgroundColor = dragAndDropActiveItemBackgroundColor,
            topAndBottomPadding = itemTopAndBottomPadding,
            leftAndRightPadding = itemLeftAndRightPadding
        ),
        checklistNewConfig = ChecklistNewRecyclerHolderConfig(
            text = textNewItem,
            textColor = textColor,
            textSize = textSize,
            textAlpha = textAlphaNewItem,
            textTypeFace = textTypeFace,
            iconTintColor = iconTintColor,
            iconAdd = iconAdd,
            iconAlphaAdd = iconAlphaAdd,
            topAndBottomPadding = itemTopAndBottomPadding,
            leftAndRightPadding = itemLeftAndRightPadding
        ),
        chipConfig = chipConfig.transform()
    )

    private fun initTextAttributes(attrs: TypedArray) {
        textColor = attrs.getColor(
            R.styleable.MaterialChecklist_text_color,
            textColor
        )

        textSize = attrs.getDimension(
            R.styleable.MaterialChecklist_text_size,
            textSize
        )

        textNewItem = attrs.getString(
            R.styleable.MaterialChecklist_text_new_item
        ) ?: textNewItem

        textAlphaCheckedItem = attrs.getFloat(
            R.styleable.MaterialChecklist_text_alpha_checked_item,
            textAlphaCheckedItem
        )

        textAlphaNewItem = attrs.getFloat(
            R.styleable.MaterialChecklist_text_alpha_new_item,
            textAlphaNewItem
        )
    }

    private fun initIconAttributes(attrs: TypedArray) {
        iconTintColor = attrs.getColor(
            R.styleable.MaterialChecklist_icon_tint_color,
            iconTintColor
        )

        iconAlphaDragIndicator = attrs.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_drag_indicator,
            iconAlphaDragIndicator
        )

        iconAlphaDelete = attrs.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_delete,
            iconAlphaDelete
        )

        iconAlphaAdd = attrs.getFloat(
            R.styleable.MaterialChecklist_icon_alpha_add,
            iconAlphaAdd
        )
    }

    private fun initCheckboxAttributes(attrs: TypedArray) {
        checkboxTintColor = attrs.getColor(
            R.styleable.MaterialChecklist_checkbox_tint_color,
            0
        ).run { if (this == 0) checkboxTintColor else this }

        checkboxAlphaCheckedItem = attrs.getFloat(
            R.styleable.MaterialChecklist_checkbox_alpha_checked_item,
            checkboxAlphaCheckedItem
        )
    }

    private fun initDragAndDropAttributes(attrs: TypedArray) {
        dragAndDropToggleBehavior =
            DragAndDropToggleBehavior.values()[attrs.getInt(
                R.styleable.MaterialChecklist_drag_and_drop_toggle_behavior,
                dragAndDropToggleBehavior.ordinal
            )]

        dragAndDropDismissKeyboardBehavior =
            DragAndDropDismissKeyboardBehavior.values()[attrs.getInt(
                R.styleable.MaterialChecklist_drag_and_drop_dismiss_keyboard_behavior,
                dragAndDropDismissKeyboardBehavior.ordinal
            )]

        dragAndDropActiveItemBackgroundColor = attrs.getColor(
            R.styleable.MaterialChecklist_drag_and_drop_item_active_background_color,
            0
        ).run { if (this == 0) dragAndDropActiveItemBackgroundColor else this }
    }

    private fun initBehaviorAttributes(attrs: TypedArray) {
        behaviorCheckedItem = BehaviorCheckedItem.values()[attrs.getInt(
            R.styleable.MaterialChecklist_behavior_checked_item,
            behaviorCheckedItem.ordinal
        )]

        behaviorUncheckedItem = BehaviorUncheckedItem.values()[attrs.getInt(
            R.styleable.MaterialChecklist_behavior_unchecked_item,
            behaviorUncheckedItem.ordinal
        )]
    }

    private fun initItemAttributes(attrs: TypedArray) {
        attrs.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_first_top)?.let {
            itemFirstTopPadding = it
        }

        attrs.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_left_and_right)?.let {
            itemLeftAndRightPadding = it
        }

        attrs.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_top_and_bottom)?.let {
            itemTopAndBottomPadding = it
        }

        attrs.getDimensionOrNull(R.styleable.MaterialChecklist_item_padding_last_bottom)?.let {
            itemLastBottomPadding = it
        }
    }

    @CheckResult
    private fun TypedArray.getDimensionOrNull(@StyleableRes index: Int): Float? {
        return getDimension(index, 0F).run { if (this != 0f) this else null }
    }
}
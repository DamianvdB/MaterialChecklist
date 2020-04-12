package com.dvdb.checklist

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.manager.config.ChecklistManagerConfig
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig
import com.dvdb.checklist.util.getColorCompat

internal class ChecklistConfig(
    context: Context,
    attrs: AttributeSet?,

    /**
     * Text
     */
    @ColorInt var textColor: Int = context.getColorCompat(R.color.cl_text_checklist_item_text_light),
    @Px var textSize: Float = context.resources.getDimension(R.dimen.item_checklist_text_size),
    var textNewItem: String = context.getString(R.string.item_checklist_new_text),
    var textAlphaCheckedItem: Float = 0.4F,
    var textAlphaNewItem: Float = 0.5F,

    /**
     * Icon
     */
    @ColorInt var iconTintColor: Int = context.getColorCompat(R.color.cl_icon_tint_light),
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
    var dragAndDropEnabled: Boolean = true,
    @ColorInt var dragAndDropActiveItemBackgroundColor: Int? = null,
    var dragAndDropActiveItemElevation: Float? = null
) {

    init {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.Checklist)

        try {
            initTextAttributes(attributes)
            initIconAttributes(attributes)
            initCheckboxAttributes(attributes)
            initDragAndDropAttributes(attributes)
        } finally {
            attributes.recycle()
        }
    }

    fun toManagerConfig() = ChecklistManagerConfig(
        dragAndDropEnabled = dragAndDropEnabled,
        adapterConfig = toAdapterConfig()
    )

    fun toAdapterConfig() = ChecklistItemAdapterConfig(
        checklistConfig = ChecklistRecyclerHolderConfig(
            textColor = textColor,
            textSize = textSize,
            textAlphaCheckedItem = textAlphaCheckedItem,
            iconTintColor = iconTintColor,
            iconAlphaDragIndicator = iconAlphaDragIndicator,
            iconAlphaDelete = iconAlphaDelete,
            iconVisibleDragIndicator = dragAndDropEnabled,
            checkboxAlphaCheckedItem = checkboxAlphaCheckedItem,
            checkboxTintColor = checkboxTintColor,
            dragActiveBackgroundColor = dragAndDropActiveItemBackgroundColor,
            dragActiveElevation = dragAndDropActiveItemElevation
        ),
        checklistNewConfig = ChecklistNewRecyclerHolderConfig(
            text = textNewItem,
            textColor = textColor,
            textSize = textSize,
            textAlpha = textAlphaNewItem,
            iconTintColor = iconTintColor,
            iconAlphaAdd = iconAlphaAdd
        )
    )

    private fun initTextAttributes(attributes: TypedArray) {
        textColor = attributes.getColor(
            R.styleable.Checklist_text_color,
            textColor
        )

        textSize = attributes.getDimension(
            R.styleable.Checklist_text_size_px,
            textSize
        )

        textNewItem = attributes.getString(
            R.styleable.Checklist_text_new_item
        ) ?: textNewItem

        textAlphaCheckedItem = attributes.getFloat(
            R.styleable.Checklist_text_alpha_checked_item,
            textAlphaCheckedItem
        )

        textAlphaNewItem = attributes.getFloat(
            R.styleable.Checklist_text_alpha_new_item,
            textAlphaNewItem
        )
    }

    private fun initIconAttributes(attributes: TypedArray) {
        iconTintColor = attributes.getColor(
            R.styleable.Checklist_icon_tint_color,
            iconTintColor
        )

        iconAlphaDragIndicator = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_drag_indicator,
            iconAlphaDragIndicator
        )

        iconAlphaDelete = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_delete,
            iconAlphaDelete
        )

        iconAlphaAdd = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_add,
            iconAlphaAdd
        )
    }

    private fun initCheckboxAttributes(attributes: TypedArray) {
        checkboxTintColor = attributes.getColor(
            R.styleable.Checklist_checkbox_tint_color,
            0
        ).run { if (this == 0) checkboxTintColor else this }

        checkboxAlphaCheckedItem = attributes.getFloat(
            R.styleable.Checklist_checkbox_alpha_checked_item,
            checkboxAlphaCheckedItem
        )
    }

    private fun initDragAndDropAttributes(attributes: TypedArray) {
        dragAndDropEnabled = attributes.getBoolean(
            R.styleable.Checklist_drag_and_drop_enabled,
            dragAndDropEnabled
        )

        dragAndDropActiveItemBackgroundColor = attributes.getColor(
            R.styleable.Checklist_drag_and_drop_item_active_background_color,
            0
        ).run { if (this == 0) dragAndDropActiveItemBackgroundColor else this }

        dragAndDropActiveItemElevation = attributes.getFloat(
            R.styleable.Checklist_drag_and_drop_item_active_elevation,
            0F
        ).run { if (this == 0F) dragAndDropActiveItemElevation else this }
    }
}
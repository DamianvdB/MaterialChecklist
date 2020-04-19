package com.dvdb.materialchecklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.config.DragAndDropToggleMode
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolderConfig

internal data class ChecklistRecyclerHolderConfig(
    @ColorInt val textColor: Int,
    @Px val textSize: Float,
    val textAlphaCheckedItem: Float,
    val textTypeFace: Typeface?,
    @ColorInt val iconTintColor: Int,
    val iconAlphaDragIndicator: Float,
    val iconAlphaDelete: Float,
    val checkboxAlphaCheckedItem: Float,
    @ColorInt val checkboxTintColor: Int?,
    val dragAndDropToggleMode: DragAndDropToggleMode,
    @ColorInt val dragAndDropActiveBackgroundColor: Int?,
    @Px val horizontalPadding: Float?
) : BaseRecyclerHolderConfig
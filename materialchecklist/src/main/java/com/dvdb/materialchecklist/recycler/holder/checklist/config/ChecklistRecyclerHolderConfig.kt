package com.dvdb.materialchecklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolderConfig

internal data class ChecklistRecyclerHolderConfig(
    @ColorInt val textColor: Int,
    @Px val textSize: Float,
    val textAlphaCheckedItem: Float,
    val textTypeFace: Typeface?,
    @ColorInt val iconTintColor: Int,
    val iconAlphaDragIndicator: Float,
    val iconAlphaDelete: Float,
    val iconVisibleDragIndicator: Boolean,
    val checkboxAlphaCheckedItem: Float,
    @ColorInt val checkboxTintColor: Int?,
    @ColorInt val dragActiveBackgroundColor: Int?,
    @Px val horizontalPadding: Float?
) : BaseRecyclerHolderConfig
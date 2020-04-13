package com.dvdb.checklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderConfig

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
    val dragActiveBackgroundColor: Int?,
    @Px val dragActiveElevation: Float?,
    @Px val horizontalPadding: Float?
) : BaseRecyclerHolderConfig
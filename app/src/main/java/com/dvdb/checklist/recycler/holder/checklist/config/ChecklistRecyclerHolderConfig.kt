package com.dvdb.checklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

data class ChecklistRecyclerHolderConfig(
    @ColorInt val textColor: Int,
    @Px val textSize: Float,
    val textAlphaCheckedItem: Float,
    @ColorInt val iconTintColor: Int,
    val iconAlphaDragIndicator: Float,
    val iconAlphaDelete: Float,
    val iconVisibleDragIndicator: Boolean,
    val checkboxAlphaCheckedItem: Float,
    @ColorInt val checkboxTintColor: Int?,
    val dragActiveBackgroundColor: Int?,
    val dragActiveElevation: Float?,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
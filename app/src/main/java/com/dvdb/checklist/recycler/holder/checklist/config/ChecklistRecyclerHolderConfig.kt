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
    val checkboxAlphaCheckedItem: Float,
    @ColorInt val checkboxTintColor: Int? = null,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
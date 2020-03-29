package com.dvdb.checklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

data class ChecklistRecyclerHolderThemeConfig(
    @ColorInt val contentUncheckedTextColor: Int,
    @ColorInt val contentCheckedTextColor: Int,
    val contentTextSizeSP: Float,

    @ColorInt val dragIndicatorTintColor: Int,
    @ColorInt val deleteTintColor: Int,

    val contentTypeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
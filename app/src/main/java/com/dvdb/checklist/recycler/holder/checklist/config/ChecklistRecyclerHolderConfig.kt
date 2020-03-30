package com.dvdb.checklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

data class ChecklistRecyclerHolderConfig(
    @ColorInt val contentUncheckedTextColor: Int,
    @ColorInt val contentCheckedTextColor: Int,

    @ColorInt val dragIndicatorTintColor: Int,
    @ColorInt val deleteTintColor: Int,

    val contentTextSizeSP: Float? = null,
    val contentTypeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
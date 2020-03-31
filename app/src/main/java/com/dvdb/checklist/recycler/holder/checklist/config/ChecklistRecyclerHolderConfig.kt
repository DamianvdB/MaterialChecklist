package com.dvdb.checklist.recycler.holder.checklist.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

data class ChecklistRecyclerHolderConfig(
    @ColorInt val uncheckedTextColor: Int,
    @ColorInt val checkedTextColor: Int,

    @ColorInt val dragIndicatorTintColor: Int,
    @ColorInt val deleteTintColor: Int,

    val textSizeSP: Float? = null,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
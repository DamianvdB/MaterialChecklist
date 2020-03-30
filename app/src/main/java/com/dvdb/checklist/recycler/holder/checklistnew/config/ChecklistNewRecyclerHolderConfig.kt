package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

class ChecklistNewRecyclerHolderConfig(
    @ColorInt val contentTextColor: Int,
    val contentTextSizeSP: Float,

    @ColorInt val addTintColor: Int,

    val contentTypeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
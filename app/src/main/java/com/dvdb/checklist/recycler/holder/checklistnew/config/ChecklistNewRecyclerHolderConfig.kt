package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

class ChecklistNewRecyclerHolderConfig(
    @ColorInt val contentTextColor: Int,

    @ColorInt val addTintColor: Int,

    val contentTextSizeSP: Float? = null,
    val contentTypeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
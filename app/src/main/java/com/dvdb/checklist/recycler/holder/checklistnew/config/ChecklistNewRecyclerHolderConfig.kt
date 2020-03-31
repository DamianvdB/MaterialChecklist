package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

class ChecklistNewRecyclerHolderConfig(
    @ColorInt val textColor: Int,

    @ColorInt val addTintColor: Int,

    val textSizeSP: Float? = null,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

class ChecklistNewRecyclerHolderConfig(
    val text: String,
    @Px val textSize: Float,
    @ColorInt val textColor: Int,
    @ColorInt val addTintColor: Int,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
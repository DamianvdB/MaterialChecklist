package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderThemeConfig

class ChecklistNewRecyclerHolderConfig(
    val text: String,
    @ColorInt val textColor: Int,
    @Px val textSize: Float,
    val textAlpha: Float,
    @ColorInt val iconTintColor: Int,
    val iconAlphaAdd: Float,
    val typeFace: Typeface? = null
) : BaseRecyclerHolderThemeConfig
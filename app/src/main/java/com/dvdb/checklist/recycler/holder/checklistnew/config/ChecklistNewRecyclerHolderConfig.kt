package com.dvdb.checklist.recycler.holder.checklistnew.config

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolderConfig

internal class ChecklistNewRecyclerHolderConfig(
    val text: String,
    @ColorInt val textColor: Int,
    @Px val textSize: Float,
    val textAlpha: Float,
    val textTypeFace: Typeface?,
    @ColorInt val iconTintColor: Int,
    val iconAlphaAdd: Float,
    @Px val horizontalPadding: Float?
) : BaseRecyclerHolderConfig
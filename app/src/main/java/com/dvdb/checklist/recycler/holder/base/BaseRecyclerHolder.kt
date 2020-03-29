package com.dvdb.checklist.recycler.holder.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem

internal abstract class BaseRecyclerHolder<T : BaseRecyclerItem, C : BaseRecyclerHolderThemeConfig>(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {
    private val context: Context = itemView.context

    abstract fun updateThemeConfig(config: C)

    abstract fun bindView(item: T)
}

interface BaseRecyclerHolderThemeConfig
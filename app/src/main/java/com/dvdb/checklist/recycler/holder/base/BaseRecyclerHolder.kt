package com.dvdb.checklist.recycler.holder.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem

internal abstract class BaseRecyclerHolder<T : BaseRecyclerItem, C : BaseRecyclerHolderThemeConfig>(
    itemView: View,
    protected var config: C
) : RecyclerView.ViewHolder(itemView) {
    private val context: Context = itemView.context

    init {
        this.initialiseView()
    }

    fun updateConfigConditionally(config: C) {
        if (this.config != config) {
            this.config = config
            initialiseView()
        }
    }

    abstract fun initialiseView()

    abstract fun bindView(item: T)
}

interface BaseRecyclerHolderThemeConfig
package com.dvdb.materialchecklist.recycler.holder.base

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem

internal abstract class BaseRecyclerHolder<T : BaseRecyclerItem, C : BaseRecyclerHolderConfig>(
    itemView: View,
    protected var config: C
) : RecyclerView.ViewHolder(itemView) {
    private val context: Context = itemView.context

    fun updateConfigConditionally(config: C) {
        if (this.config != config) {
            this.config = config
            onConfigUpdated()
        }
    }

    abstract fun bindView(item: T)

    abstract fun onConfigUpdated()
}

internal interface BaseRecyclerHolderConfig : Config
package com.dvdb.materialchecklist.recycler.holder.base.factory

import android.view.ViewGroup
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem

internal interface BaseRecyclerHolderFactory<T : BaseRecyclerItem, C : BaseRecyclerHolderConfig> {
    fun create(parent: ViewGroup, config: C): BaseRecyclerHolder<T, C>
}
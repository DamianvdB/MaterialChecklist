package com.dvdb.checklist.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.recycler.adapter.base.BaseRecyclerAdapter
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.checklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem

internal class ChecklistItemAdapter(
    config: ChecklistItemAdapterConfig,
    private val itemRecyclerHolderFactory: ChecklistRecyclerHolder.Factory,
    private val itemNewRecyclerHolderFactory: ChecklistNewRecyclerHolder.Factory,
    items: List<BaseRecyclerItem> = emptyList()
) : BaseRecyclerAdapter<BaseRecyclerItem>(items) {

    var config: ChecklistItemAdapterConfig = config
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun getItemViewType(position: Int) = _items[position].type.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            BaseRecyclerItem.Type.CHECKLIST.ordinal -> itemRecyclerHolderFactory.create(parent, config.checklistConfig)
            BaseRecyclerItem.Type.CHECKLIST_NEW.ordinal -> itemNewRecyclerHolderFactory.create(parent, config.checklistNewConfig)
            else -> error("Unknown item view type. Must be of type 'CHECKLIST', 'CHECKLIST_NEW' or 'CHECKLIST_DIVIDER'")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = _items[position]
        if (holder is ChecklistRecyclerHolder && item is ChecklistRecyclerItem) {
            holder.updateConfigConditionally(config.checklistConfig)
            holder.bindView(item)
        } else if (holder is ChecklistNewRecyclerHolder && item is ChecklistNewRecyclerItem) {
            holder.updateConfigConditionally(config.checklistNewConfig)
            holder.bindView(item)
        } else {
            error("Unknown holder. Must be of type 'ChecklistRecyclerHolder' or 'ChecklistNewRecyclerHolder'")
        }
    }
}
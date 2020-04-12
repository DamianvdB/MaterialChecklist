package com.dvdb.checklist.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.recycler.adapter.base.BaseRecyclerAdapter
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.checklist.recycler.holder.DraggableRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.checklist.recycler.util.ItemTouchHelperAdapter

internal class ChecklistItemAdapter(
    config: ChecklistItemAdapterConfig,
    private val itemRecyclerHolderFactory: ChecklistRecyclerHolder.Factory,
    private val itemNewRecyclerHolderFactory: ChecklistNewRecyclerHolder.Factory,
    private val itemDragListener: ChecklistItemAdapterDragListener,
    items: List<BaseRecyclerItem> = emptyList()
) : BaseRecyclerAdapter<BaseRecyclerItem>(items),
    ItemTouchHelperAdapter {

    var config: ChecklistItemAdapterConfig = config
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    var requestFocus: ChecklistItemAdapterRequestFocus = ChecklistItemAdapterRequestFocus(RecyclerView.NO_POSITION)
        set(value) {
            if (field != value) {
                field = value
                notifyItemChanged(value.position)
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

            if (position == requestFocus.position) {
                holder.requestFocus(
                    requestFocus.isStartSelection,
                    requestFocus.isShowKeyboard
                )
            }

        } else if (holder is ChecklistNewRecyclerHolder && item is ChecklistNewRecyclerItem) {
            holder.updateConfigConditionally(config.checklistNewConfig)
            holder.bindView(item)

        } else {
            error("Unknown holder. Must be of type 'ChecklistRecyclerHolder' or 'ChecklistNewRecyclerHolder'")
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        return itemDragListener.onItemMove(fromPosition, toPosition)
    }

    override fun canDragOverTarget(current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return itemDragListener.canDragOverTargetItem(current.adapterPosition, target.adapterPosition)
    }

    override fun onDragStart(viewHolder: RecyclerView.ViewHolder) {
        itemDragListener.onItemDragStart()

        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStart()
        }
    }

    override fun onDragStop(viewHolder: RecyclerView.ViewHolder) {
        itemDragListener.onItemDragStop()

        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStop()
        }
    }
}
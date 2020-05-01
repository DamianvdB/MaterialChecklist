/*
 * Designed and developed by Damian van den Berg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvdb.materialchecklist.recycler.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.recycler.adapter.base.BaseRecyclerAdapter
import com.dvdb.materialchecklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.holder.DraggableRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.util.ItemTouchHelperAdapter

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
            field = value
            notifyItemChanged(value.position)
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = items[position].id

    override fun getItemViewType(position: Int): Int = _items[position].type.ordinal

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
                    requestFocus.selectionPosition,
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
        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStart()
            itemDragListener.onItemDragStarted()
        }
    }

    override fun onDragStop(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStop()
            itemDragListener.onItemDragStopped()
        }
    }
}
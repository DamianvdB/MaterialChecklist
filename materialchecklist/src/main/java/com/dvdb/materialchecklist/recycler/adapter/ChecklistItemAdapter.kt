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
import com.dvdb.materialchecklist.recycler.adapter.listener.ChecklistItemAdapterDragListener
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.base.adapter.BaseRecyclerAdapter
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.checklist.holder.ChecklistRecyclerHolder
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.checklistnew.holder.ChecklistNewRecyclerHolder
import com.dvdb.materialchecklist.recycler.checklistnew.model.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.content.holder.ContentRecyclerHolder
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerItem
import com.dvdb.materialchecklist.recycler.title.holder.TitleRecyclerHolder
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import com.dvdb.materialchecklist.recycler.util.ItemTouchHelperAdapter
import com.dvdb.materialchecklist.recycler.util.holder.DraggableRecyclerHolder
import com.dvdb.materialchecklist.recycler.util.holder.RequestFocusRecyclerHolder

internal class ChecklistItemAdapter(
    config: ChecklistItemAdapterConfig,
    private val itemTitleRecyclerHolderFactory: TitleRecyclerHolder.Factory,
    private val itemContentRecyclerHolderFactory: ContentRecyclerHolder.Factory,
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

    var requestFocus: ChecklistItemAdapterRequestFocus =
        ChecklistItemAdapterRequestFocus(RecyclerView.NO_POSITION)
        set(value) {
            field = value
            notifyItemChanged(value.position)
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long = _items[position].id

    override fun getItemViewType(position: Int): Int = _items[position].type.ordinal

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (BaseRecyclerItem.Type.fromInt(viewType)) {
            BaseRecyclerItem.Type.TITLE -> itemTitleRecyclerHolderFactory.create(
                parent,
                config.titleConfig
            )
            BaseRecyclerItem.Type.CONTENT -> itemContentRecyclerHolderFactory.create(
                parent,
                config.contentConfig
            )
            BaseRecyclerItem.Type.CHECKLIST -> itemRecyclerHolderFactory.create(
                parent,
                config.checklistConfig
            )
            BaseRecyclerItem.Type.CHECKLIST_NEW -> itemNewRecyclerHolderFactory.create(
                parent,
                config.checklistNewConfig
            )
            else -> error("Unknown item view type. Must be of type 'CHECKLIST' or 'CHECKLIST_NEW'")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = _items[position]

        if (holder is TitleRecyclerHolder && item is TitleRecyclerItem) {
            holder.updateConfigConditionally(config.titleConfig)
            holder.bindView(item)

        } else if (holder is ContentRecyclerHolder && item is ContentRecyclerItem) {
            holder.updateConfigConditionally(config.contentConfig)
            holder.bindView(item)

        } else if (holder is ChecklistRecyclerHolder && item is ChecklistRecyclerItem) {
            holder.updateConfigConditionally(config.checklistConfig)
            holder.bindView(item)

        } else if (holder is ChecklistNewRecyclerHolder && item is ChecklistNewRecyclerItem) {
            holder.updateConfigConditionally(config.checklistNewConfig)
            holder.bindView(item)

        } else {
            error("Unknown holder. Must be of type 'ChecklistRecyclerHolder' or 'ChecklistNewRecyclerHolder'")
        }

        if (holder is RequestFocusRecyclerHolder && position == requestFocus.position) {
            holder.requestFocus(
                requestFocus.selectionPosition,
                requestFocus.isShowKeyboard
            )
        }
    }

    override fun onItemMove(
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        return itemDragListener.onItemMove(fromPosition, toPosition)
    }

    override fun canDragOverTarget(
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return itemDragListener.canDragOverTargetItem(
            current.adapterPosition,
            target.adapterPosition
        )
    }

    override fun onDragStart(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStart()
            itemDragListener.onItemDragStarted(viewHolder.adapterPosition)
        }
    }

    override fun onDragStop(viewHolder: RecyclerView.ViewHolder) {
        if (viewHolder is DraggableRecyclerHolder) {
            viewHolder.onDragStop()
            itemDragListener.onItemDragStopped(viewHolder.adapterPosition)
        }
    }
}
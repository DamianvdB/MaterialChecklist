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

package com.dvdb.materialchecklist.manager.content

import com.dvdb.materialchecklist.manager.content.model.ContentManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerItem

private const val NO_POSITION = -1

internal class ContentManagerImpl : ContentManager {

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: ContentManagerConfig

    private var hasFocus: Boolean = false

    override fun lateInitContentState(
        adapter: ChecklistItemAdapter,
        config: ContentManagerConfig
    ) {
        this.adapter = adapter
        this.config = config
    }

    override fun setContentConfig(config: ContentManagerConfig) {
        if (this.config.adapterConfig != config.adapterConfig) {
            adapter.config = config.adapterConfig
        }

        this.config = config
    }

    override fun getContentItem(): String? {
        val item = adapter.items.firstOrNull { it is ContentRecyclerItem }
        return (item as? ContentRecyclerItem)?.text
    }

    override fun setContentItem(text: String) {
        val newItem = ContentRecyclerItem(text)
        val position = adapter.items.indexOfFirst { it is ContentRecyclerItem }

        if (position != NO_POSITION) {
            updateItemInAdapter(
                newItem,
                position
            )
        } else {
            addItemToAdapter(
                newItem,
                adapter.itemCount
            )
        }
    }

    override fun removeContentItem(): Boolean {
        val position = adapter.items.indexOfFirst { it is ContentRecyclerItem }

        return if (position != NO_POSITION) {
            removeItemFromAdapter(position)
            true
        } else {
            false
        }
    }

    override fun requestContentItemFocus(): Boolean {
        val position = adapter.items.indexOfFirst { it is ContentRecyclerItem }

        return if (position != NO_POSITION) {
            adapter.requestFocus = ChecklistItemAdapterRequestFocus(
                position = position,
                selectionPosition = Int.MAX_VALUE,
                isShowKeyboard = true
            )
            true
        } else {
            false
        }
    }

    override fun onContentItemTextChanged(
        position: Int,
        text: String
    ) {
        val item = adapter.items.getOrNull(position)

        if (item is ContentRecyclerItem) {
            updateItemInAdapter(
                item = item.copy(text),
                position = position,
                notify = false
            )
        }
    }

    override fun onContentItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        this.hasFocus = hasFocus
    }

    override fun onItemMove(
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        return false
    }

    override fun canDragOverTargetItem(
        currentPosition: Int,
        targetPosition: Int
    ): Boolean {
        return false
    }

    private fun addItemToAdapter(
        item: BaseRecyclerItem,
        position: Int
    ) {
        adapter.addItem(
            item = item,
            position = position
        )
    }

    private fun updateItemInAdapter(
        item: BaseRecyclerItem,
        position: Int,
        notify: Boolean = true
    ) {
        adapter.updateItem(
            item = item,
            position = position,
            notify = notify
        )
    }

    private fun removeItemFromAdapter(position: Int) {
        adapter.removeItem(position)
    }
}
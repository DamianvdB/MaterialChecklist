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

package com.dvdb.materialchecklist.manager

import com.dvdb.materialchecklist.manager.config.TitleManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.title.TitleRecyclerItem

private const val NO_POSITION = -1

internal class TitleManagerImpl : TitleManager {

    override var onTitleItemEnterKeyPressed: () -> Unit = {}
    override var onTitleItemActionIconClicked: () -> Unit = {}

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: TitleManagerConfig

    private var hasFocus: Boolean = false

    override fun lateInitTitleState(
        adapter: ChecklistItemAdapter,
        config: TitleManagerConfig
    ) {
        this.adapter = adapter
        this.config = config
    }

    override fun setTitleConfig(config: TitleManagerConfig) {
        if (this.config.adapterConfig != config.adapterConfig) {
            adapter.config = config.adapterConfig
        }

        this.config = config
    }

    override fun getTitleItem(): String? {
        val item = adapter.items.firstOrNull { it is TitleRecyclerItem }
        return (item as? TitleRecyclerItem)?.text
    }

    override fun setTitleItem(text: String) {
        val newItem = TitleRecyclerItem(text)
        val position = adapter.items.indexOfFirst { it is TitleRecyclerItem }

        if (position != NO_POSITION) {
            updateItemInAdapter(
                newItem,
                position
            )
        } else {
            addItemToAdapter(
                TitleRecyclerItem(text = text),
                0
            )
        }
    }

    override fun removeTitleItem(): Boolean {
        val position = adapter.items.indexOfFirst { it is TitleRecyclerItem }

        return if (position != NO_POSITION) {
            removeItemFromAdapter(position)
            true
        } else {
            false
        }
    }

    override fun onTitleItemEnterKeyPressed(position: Int) {
        onTitleItemEnterKeyPressed()
    }

    override fun onTitleItemTextChanged(
        position: Int,
        text: String
    ) {
        val item = adapter.items.getOrNull(position)

        if (item is TitleRecyclerItem) {
            updateItemInAdapter(
                item = item.copy(text),
                position = position,
                notify = false
            )
        }
    }

    override fun onTitleItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        this.hasFocus = hasFocus
    }

    override fun onTitleItemActionIconClicked(position: Int) {
        onTitleItemActionIconClicked()
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

    private fun setItemsInAdapter(
        items: List<BaseRecyclerItem>,
        notify: Boolean = true
    ) {
        adapter.setItems(
            items = items,
            notify = notify
        )
    }

    private fun addItemToAdapter(
        item: TitleRecyclerItem,
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
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

package com.dvdb.materialchecklist.manager.chip

import com.dvdb.materialchecklist.manager.chip.model.ChipItem
import com.dvdb.materialchecklist.manager.chip.model.ChipManagerConfig
import com.dvdb.materialchecklist.manager.chip.model.transform
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.chip.model.ChipContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.chip.model.ChipRecyclerItem

private const val NO_POSITION = -1

internal class ChipManagerImpl : ChipManager {

    override var onItemClicked: (item: ChipItem) -> Unit = {}

    override var onItemInContainerClicked: (id: Int) -> Unit = { id ->
        val chipItemPredicate: (ChipRecyclerItem) -> Boolean = { item -> item.id == id }
        val containerItem = adapter.items.firstOrNull {
            (it as? ChipContainerRecyclerItem)?.items
                ?.any(chipItemPredicate)
                ?: false
        }

        if (containerItem is ChipContainerRecyclerItem) {
            val item = containerItem.items.first(chipItemPredicate)
            onItemClicked(item.transform())
        }
    }
    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: ChipManagerConfig

    override fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChipManagerConfig
    ) {
        this.adapter = adapter
        this.config = config
    }

    override fun setConfig(config: ChipManagerConfig) {
        if (this.config.adapterConfig != config.adapterConfig) {
            adapter.config = config.adapterConfig
        }

        this.config = config
    }

    override fun getChipItems(): List<ChipItem> {
        val item = adapter.items.firstOrNull { it is ChipContainerRecyclerItem }
        return (item as? ChipContainerRecyclerItem)?.items?.map { it.transform() } ?: emptyList()
    }

    override fun setChipItems(items: List<ChipItem>) {
        val newItem = ChipContainerRecyclerItem(items = items.map { it.transform() })
        val position = adapter.items.indexOfFirst { it is ChipContainerRecyclerItem }

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

    override fun removeChipItems(): Boolean {
        val position = adapter.items.indexOfFirst { it is ChipContainerRecyclerItem }

        return if (position != NO_POSITION) {
            removeItemFromAdapter(position)
            true
        } else {
            false
        }
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
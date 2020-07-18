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

package com.dvdb.materialchecklist.manager.image

import com.dvdb.materialchecklist.manager.image.model.ImageItem
import com.dvdb.materialchecklist.manager.image.model.ImageManagerConfig
import com.dvdb.materialchecklist.manager.image.model.transform
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerItem
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerItem

private const val NO_POSITION = -1

internal class ImageManagerImpl : ImageManager {

    override var onImageItemClicked: (item: ImageItem) -> Unit = {}

    override val onImageItemInContainerClicked: (item: ImageRecyclerItem) -> Unit = {
        onImageItemClicked(it.transform())
    }

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: ImageManagerConfig

    override fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ImageManagerConfig
    ) {
        this.adapter = adapter
        this.config = config
    }

    override fun setConfig(config: ImageManagerConfig) {
        if (this.config.adapterConfig != config.adapterConfig) {
            adapter.config = config.adapterConfig
        }

        this.config = config
    }

    override fun getImageItems(): List<ImageItem> {
        val item = adapter.items.firstOrNull { it is ImageContainerRecyclerItem }
        return (item as? ImageContainerRecyclerItem)?.items?.map { it.transform() } ?: emptyList()
    }

    override fun setImageItems(items: List<ImageItem>) {
        val newItem = ImageContainerRecyclerItem(items = items.map { it.transform() })
        val position = adapter.items.indexOfFirst { it is ImageContainerRecyclerItem }

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

    override fun removeImageItems(): Boolean {
        val position = adapter.items.indexOfFirst { it is ImageContainerRecyclerItem }

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
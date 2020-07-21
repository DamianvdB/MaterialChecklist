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

import com.dvdb.materialchecklist.config.ChecklistConfig
import com.dvdb.materialchecklist.manager.base.BaseItem
import com.dvdb.materialchecklist.manager.checklist.ChecklistManager
import com.dvdb.materialchecklist.manager.chip.ChipManager
import com.dvdb.materialchecklist.manager.chip.model.ChipItemContainer
import com.dvdb.materialchecklist.manager.chip.model.transform
import com.dvdb.materialchecklist.manager.content.ContentManager
import com.dvdb.materialchecklist.manager.image.ImageManager
import com.dvdb.materialchecklist.manager.image.model.ImageItemContainer
import com.dvdb.materialchecklist.manager.image.model.transform
import com.dvdb.materialchecklist.manager.title.TitleManager
import com.dvdb.materialchecklist.manager.title.model.TitleItem
import com.dvdb.materialchecklist.manager.title.model.transform
import com.dvdb.materialchecklist.manager.util.model.RequestFocus
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import com.dvdb.materialchecklist.util.exhaustive

internal class Manager(
    private val titleManager: TitleManager,
    private val contentManager: ContentManager,
    private val checklistManager: ChecklistManager,
    private val chipManager: ChipManager,
    private val imageManager: ImageManager
) : TitleManager by titleManager,
    ContentManager by contentManager,
    ChecklistManager by checklistManager,
    ChipManager by chipManager,
    ImageManager by imageManager {

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var adapterConfig: ChecklistItemAdapterConfig

    fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit,
        enableItemAnimations: (isEnabled: Boolean) -> Unit
    ) {
        this.adapter = adapter
        this.adapterConfig = config.toAdapterConfig()

        val updateItemInAdapterSilently: (item: BaseRecyclerItem, position: Int) -> Unit =
            { item, position ->
                adapter.updateItem(
                    item,
                    position,
                    false
                )
            }

        val items: () -> List<BaseRecyclerItem> = {
            adapter.items
        }

        titleManager.lateInitState(
            items,
            updateItemInAdapterSilently
        )

        contentManager.lateInitState(
            adapter = adapter,
            config = config.toContentManagerConfig()
        )

        checklistManager.lateInitState(
            adapter = adapter,
            config = config.toManagerConfig(),
            scrollToPosition = scrollToPosition,
            startDragAndDrop = startDragAndDrop,
            enableDragAndDrop = enableDragAndDrop,
            updateItemPadding = updateItemPadding,
            enableItemAnimations = enableItemAnimations
        )
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    fun setItems(items: List<BaseItem>) {
        if (this::adapter.isInitialized) {
            val recyclerItems: MutableList<BaseRecyclerItem> = mutableListOf()
            var requestFocusForItem: Pair<Int, RequestFocus.Perform>? = null

            items.forEachIndexed { index, item ->
                when (item.type) {
                    BaseItem.Type.TITLE -> {
                        val titleItem = item as TitleItem
                        if (titleItem.requestFocus is RequestFocus.Perform) {
                            requestFocusForItem = Pair(index, titleItem.requestFocus)
                        }
                        recyclerItems.add(titleItem.transform())
                    }

                    BaseItem.Type.IMAGE_CONTAINER -> {
                        recyclerItems.add((item as ImageItemContainer).transform())
                    }

                    BaseItem.Type.CHIP_CONTAINER -> {
                        recyclerItems.add((item as ChipItemContainer).transform())
                    }
                }.exhaustive
            }

            requestFocusForItem?.let { (position, requestFocus) ->
                adapter.requestFocusOnce = ChecklistItemAdapterRequestFocus(
                    position,
                    requestFocus.selectionPosition,
                    requestFocus.isShowKeyboard
                )
            }

            adapter.setItems(recyclerItems)
        }
    }

    fun getItems(
        keepCheckboxSymbolsOfChecklistItems: Boolean,
        keepCheckedItems: Boolean
    ): List<BaseItem> {
        val items: MutableList<BaseItem> = mutableListOf()

        adapter.items.forEach { item ->
            when (item.type) {
                BaseRecyclerItem.Type.TITLE -> {
                    items.add((item as TitleRecyclerItem).transform())
                }
                BaseRecyclerItem.Type.CONTENT -> {
                }
                BaseRecyclerItem.Type.CHECKLIST -> {
                }
                BaseRecyclerItem.Type.CHECKLIST_NEW -> {
                }
                BaseRecyclerItem.Type.CHIP -> {
                }
                BaseRecyclerItem.Type.IMAGE -> {
                    items.add((item as ImageContainerRecyclerItem).transform())
                }
            }
        }

        return items
    }

    fun getItemCount(): Int {
        return adapter.itemCount
    }

    fun setConfig(config: ChecklistConfig) {
        val latestAdapterConfig = config.toAdapterConfig()
        if (adapterConfig != latestAdapterConfig) {
            adapterConfig = latestAdapterConfig
            adapter.config = latestAdapterConfig
        }

        contentManager.setConfig(config.toContentManagerConfig())
        checklistManager.setConfig(config.toManagerConfig())
    }

    override fun onItemMove(
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        return executeActionForRecyclerItemType(
            item = adapter.items.getOrNull(fromPosition),
            titleItemAction = {
                titleManager.onItemMove(
                    fromPosition,
                    toPosition
                )
            },
            contentItemAction = {
                contentManager.onItemMove(
                    fromPosition,
                    toPosition
                )
            },
            checklistItemAction = {
                checklistManager.onItemMove(
                    fromPosition,
                    toPosition
                )
            },
            chipItemAction = {
                chipManager.onItemMove(
                    fromPosition,
                    toPosition
                )
            },
            imageItemAction = {
                imageManager.onItemMove(
                    fromPosition,
                    toPosition
                )
            },
            defaultAction = { false }
        ) as Boolean
    }

    override fun canDragOverTargetItem(
        currentPosition: Int,
        targetPosition: Int
    ): Boolean {
        return executeActionForRecyclerItemType(
            item = adapter.items.getOrNull(currentPosition),
            titleItemAction = {
                titleManager.canDragOverTargetItem(
                    currentPosition,
                    targetPosition
                )
            },
            contentItemAction = {
                contentManager.canDragOverTargetItem(
                    currentPosition,
                    targetPosition
                )
            },
            checklistItemAction = {
                checklistManager.canDragOverTargetItem(
                    currentPosition,
                    targetPosition
                )
            },
            chipItemAction = {
                chipManager.canDragOverTargetItem(
                    currentPosition,
                    targetPosition
                )
            },
            imageItemAction = {
                imageManager.canDragOverTargetItem(
                    currentPosition,
                    targetPosition
                )
            },
            defaultAction = { false }
        ) as Boolean
    }

    override fun onItemDragStarted(position: Int) {
        executeActionForRecyclerItemType(
            item = adapter.items.getOrNull(position),
            titleItemAction = { titleManager.onItemDragStarted(position) },
            contentItemAction = { contentManager.onItemDragStarted(position) },
            checklistItemAction = { checklistManager.onItemDragStarted(position) },
            chipItemAction = { chipManager.onItemDragStarted(position) },
            imageItemAction = { imageManager.onItemDragStarted(position) }
        )
    }

    override fun onItemDragStopped(position: Int) {
        executeActionForRecyclerItemType(
            item = adapter.items.getOrNull(position),
            titleItemAction = { titleManager.onItemDragStopped(position) },
            contentItemAction = { contentManager.onItemDragStopped(position) },
            checklistItemAction = { checklistManager.onItemDragStopped(position) },
            chipItemAction = { chipManager.onItemDragStopped(position) },
            imageItemAction = { imageManager.onItemDragStopped(position) }
        )
    }

    private fun executeActionForRecyclerItemType(
        item: BaseRecyclerItem?,
        titleItemAction: () -> Any,
        contentItemAction: () -> Any,
        checklistItemAction: () -> Any,
        chipItemAction: () -> Any,
        imageItemAction: () -> Any,
        defaultAction: () -> Any = {}
    ): Any {
        if (item == null) {
            return defaultAction
        }

        return when (item.type) {
            BaseRecyclerItem.Type.TITLE -> titleItemAction()
            BaseRecyclerItem.Type.CONTENT -> contentItemAction()
            BaseRecyclerItem.Type.CHECKLIST,
            BaseRecyclerItem.Type.CHECKLIST_NEW -> checklistItemAction()
            BaseRecyclerItem.Type.CHIP -> chipItemAction()
            BaseRecyclerItem.Type.IMAGE -> imageItemAction()
        }
    }
}
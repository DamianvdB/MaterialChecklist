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
import com.dvdb.materialchecklist.manager.checklist.ChecklistManager
import com.dvdb.materialchecklist.manager.checklist.model.transform
import com.dvdb.materialchecklist.manager.chip.ChipManager
import com.dvdb.materialchecklist.manager.chip.model.transform
import com.dvdb.materialchecklist.manager.content.ContentManager
import com.dvdb.materialchecklist.manager.content.model.transform
import com.dvdb.materialchecklist.manager.image.ImageManager
import com.dvdb.materialchecklist.manager.image.model.transform
import com.dvdb.materialchecklist.manager.model.*
import com.dvdb.materialchecklist.manager.title.TitleManager
import com.dvdb.materialchecklist.manager.title.model.transform
import com.dvdb.materialchecklist.manager.util.ManagerConstants
import com.dvdb.materialchecklist.manager.util.model.RequestFocus
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterConfig
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerItem
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import com.dvdb.materialchecklist.util.DelayHandler
import com.dvdb.materialchecklist.util.exhaustive

private const val ENABLE_ITEM_ANIMATIONS_DELAY_MS = 100L

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
    private lateinit var enableItemAnimations: (isEnabled: Boolean) -> Unit

    private val delayHandler: DelayHandler = DelayHandler()

    private val originalItemIds: MutableMap<Long, Int> = mutableMapOf()
    private var checklistItemContainerId: Int = 0

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
        this.enableItemAnimations = enableItemAnimations

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

        titleManager.lateInitTitleState(
            items,
            updateItemInAdapterSilently
        )

        contentManager.lateInitContentState(
            items,
            updateItemInAdapterSilently
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
            originalItemIds.clear()
            val recyclerItems: MutableList<BaseRecyclerItem> = mutableListOf()
            var requestFocusForItem: Pair<Int, RequestFocus.Perform>? = null

            items.forEachIndexed { index, item ->
                when (item) {
                    is TitleItem -> {
                        if (item.requestFocus is RequestFocus.Perform) {
                            requestFocusForItem = Pair(index, item.requestFocus)
                        }
                        recyclerItems.add(item.transform())
                    }

                    is ContentItem -> {
                        if (item.requestFocus is RequestFocus.Perform) {
                            requestFocusForItem = Pair(index, item.requestFocus)
                        }
                        recyclerItems.add(item.transform())
                    }

                    is ImageItemContainer -> {
                        recyclerItems.add(item.transform())
                    }

                    is ChipItemContainer -> {
                        recyclerItems.add(item.transform())
                    }

                    is ChecklistItemContainer -> {
                        if (item.requestFocus is RequestFocus.Perform) {
                            requestFocusForItem = Pair(index, item.requestFocus)
                        }
                        recyclerItems.addAll(item.transform())
                    }
                }.exhaustive

                if (item is ChecklistItemContainer) {
                    checklistItemContainerId = item.id
                } else {
                    originalItemIds[recyclerItems.last().id] = item.id
                }
            }

            requestFocusForItem?.let { (position, requestFocus) ->
                adapter.requestFocusOnce = ChecklistItemAdapterRequestFocus(
                    position,
                    requestFocus.selectionPosition,
                    requestFocus.isShowKeyboard
                )
            }

            enableItemAnimations(false)

            adapter.setItems(recyclerItems)

            delayHandler.run(ENABLE_ITEM_ANIMATIONS_DELAY_MS) {
                enableItemAnimations(true)
            }
        }
    }

    fun getItems(
        keepCheckboxSymbolsOfChecklistItems: Boolean,
        keepCheckedItems: Boolean
    ): List<BaseItem> {
        var addedChecklistItems = false

        return adapter.items.map { item ->
            if (item.type == BaseRecyclerItem.Type.CHECKLIST) {
                if (!addedChecklistItems) {
                    addedChecklistItems = true
                } else {
                    return@map null
                }
            }
            return@map item.transformToBaseItem(
                keepCheckboxSymbolsOfChecklistItems,
                keepCheckedItems
            )
        }.filterNotNull()
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

        checklistManager.setConfig(config.toManagerConfig())
    }

    fun getItemWithFocus(): BaseItem? {
        val position = titleManager.getTitleItemFocusPosition().takeIf {
            it != ManagerConstants.NO_POSITION
        } ?: contentManager.getContentItemFocusPosition().takeIf {
            it != ManagerConstants.NO_POSITION
        } ?: checklistManager.getItemFocusPosition()

        return adapter.items.getOrNull(position)?.transformToBaseItem(
            keepCheckboxSymbolsOfChecklistItems = true,
            keepCheckedItems = true
        )
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

    private fun BaseRecyclerItem.transformToBaseItem(
        keepCheckboxSymbolsOfChecklistItems: Boolean,
        keepCheckedItems: Boolean
    ): BaseItem? {
        val originalItemId = originalItemIds[id] ?: id.toInt()

        return when (type) {
            BaseRecyclerItem.Type.TITLE -> {
                (this as TitleRecyclerItem).transform()
                    .copy(id = originalItemId)
            }
            BaseRecyclerItem.Type.CONTENT -> {
                (this as ContentRecyclerItem).transform()
                    .copy(id = originalItemId)
            }
            BaseRecyclerItem.Type.CHECKLIST -> {
                val checklistItemsText: String = checklistManager.getFormattedTextItems(
                    keepCheckboxSymbolsOfChecklistItems,
                    keepCheckedItems
                )
                ChecklistItemContainer(
                    checklistItemContainerId,
                    checklistItemsText
                )
            }
            BaseRecyclerItem.Type.CHECKLIST_NEW -> {
                null
            }
            BaseRecyclerItem.Type.CHIP -> {
                (this as ChipContainerRecyclerItem).transform()
                    .copy(id = originalItemId)
            }
            BaseRecyclerItem.Type.IMAGE -> {
                (this as ImageContainerRecyclerItem).transform()
                    .copy(id = originalItemId)
            }
        }
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
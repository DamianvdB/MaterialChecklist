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
import com.dvdb.materialchecklist.manager.content.ContentManager
import com.dvdb.materialchecklist.manager.title.TitleManager
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem

internal class Manager(
    private val titleManager: TitleManager,
    private val contentManager: ContentManager,
    private val checklistManager: ChecklistManager,
    private val items: () -> List<BaseRecyclerItem>
) : TitleManager by titleManager,
    ContentManager by contentManager,
    ChecklistManager by checklistManager {

    fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit,
        enableItemAnimations: (isEnabled: Boolean) -> Unit
    ) {
        titleManager.lateInitTitleState(
            adapter = adapter,
            config = config.totTitleManagerConfig()
        )

        contentManager.lateInitContentState(
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

    fun getItemCount(): Int {
        return items().size
    }

    fun setConfig(config: ChecklistConfig) {
        titleManager.setTitleConfig(config.totTitleManagerConfig())
        contentManager.setContentConfig(config.toContentManagerConfig())
        checklistManager.setConfig(config.toManagerConfig())
    }

    override fun onItemMove(
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        return executeActionForRecyclerItemType(
            item = items().getOrNull(fromPosition),
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
            defaultAction = { false }
        ) as Boolean
    }

    override fun canDragOverTargetItem(
        currentPosition: Int,
        targetPosition: Int
    ): Boolean {
        return executeActionForRecyclerItemType(
            item = items().getOrNull(currentPosition),
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
            defaultAction = { false }
        ) as Boolean
    }

    override fun onItemDragStarted(position: Int) {
        executeActionForRecyclerItemType(
            item = items().getOrNull(position),
            titleItemAction = { titleManager.onItemDragStarted(position) },
            contentItemAction = { contentManager.onItemDragStarted(position) },
            checklistItemAction = { checklistManager.onItemDragStarted(position) }
        )
    }

    override fun onItemDragStopped(position: Int) {
        executeActionForRecyclerItemType(
            item = items().getOrNull(position),
            titleItemAction = { titleManager.onItemDragStopped(position) },
            contentItemAction = { contentManager.onItemDragStopped(position) },
            checklistItemAction = { checklistManager.onItemDragStopped(position) }
        )
    }

    private fun executeActionForRecyclerItemType(
        item: BaseRecyclerItem?,
        titleItemAction: () -> Any,
        contentItemAction: () -> Any,
        checklistItemAction: () -> Any,
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
        }
    }
}
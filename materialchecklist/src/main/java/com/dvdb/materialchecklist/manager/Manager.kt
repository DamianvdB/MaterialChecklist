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

import com.dvdb.materialchecklist.manager.checklist.ChecklistManager
import com.dvdb.materialchecklist.manager.title.TitleManager
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.item.title.TitleRecyclerItem

internal class Manager(
    private val titleManager: TitleManager,
    private val checklistManager: ChecklistManager,
    private val items: () -> List<BaseRecyclerItem>
) : TitleManager by titleManager,
    ChecklistManager by checklistManager {

    fun getItemCount(): Int {
        return items().size
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
            checklistItemAction = { checklistManager.onItemDragStarted(position) }
        )
    }

    override fun onItemDragStopped(position: Int) {
        executeActionForRecyclerItemType(
            item = items().getOrNull(position),
            titleItemAction = { titleManager.onItemDragStopped(position) },
            checklistItemAction = { checklistManager.onItemDragStopped(position) }
        )
    }

    private fun executeActionForRecyclerItemType(
        item: BaseRecyclerItem?,
        titleItemAction: () -> Any,
        checklistItemAction: () -> Any,
        defaultAction: () -> Any = {}
    ): Any {
        return when (item) {
            is TitleRecyclerItem -> titleItemAction()

            is ChecklistRecyclerItem,
            is ChecklistNewRecyclerItem -> checklistItemAction()

            else -> defaultAction()
        }
    }
}
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

import android.widget.TextView
import com.dvdb.materialchecklist.manager.config.ChecklistManagerConfig
import com.dvdb.materialchecklist.manager.config.TitleManagerConfig
import com.dvdb.materialchecklist.manager.item.ChecklistItem
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.item.title.TitleRecyclerItem

internal class ManagerImpl(
    private val titleManager: TitleManager,
    private val checklistManager: ChecklistManager,
    private val items: () -> List<BaseRecyclerItem>
) : Manager {

    override var onTitleItemEnterKeyPressed: () -> Unit
        get() = titleManager.onTitleItemEnterKeyPressed
        set(value) {
            titleManager.onTitleItemEnterKeyPressed = value
        }

    override var onTitleItemActionIconClicked: () -> Unit
        get() = titleManager.onTitleItemActionIconClicked
        set(value) {
            titleManager.onTitleItemActionIconClicked = value
        }

    override fun lateInitTitleState(
        adapter: ChecklistItemAdapter,
        config: TitleManagerConfig
    ) {
        titleManager.lateInitTitleState(
            adapter,
            config
        )
    }

    override fun getTitleItem(): String? {
        return titleManager.getTitleItem()
    }

    override fun setTitleConfig(config: TitleManagerConfig) {
        titleManager.setTitleConfig(config)
    }

    override fun setTitleItem(text: String) {
        titleManager.setTitleItem(text)
    }

    override fun removeTitleItem(): Boolean {
        return titleManager.removeTitleItem()
    }

    override fun onTitleItemEnterKeyPressed(position: Int) {
        titleManager.onTitleItemEnterKeyPressed(position)
    }

    override fun onTitleItemTextChanged(
        position: Int,
        text: String
    ) {
        titleManager.onTitleItemTextChanged(
            position,
            text
        )
    }

    override fun onTitleItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        titleManager.onTitleItemFocusChanged(
            position,
            startSelection,
            endSelection,
            hasFocus
        )
    }

    override fun onTitleItemActionIconClicked(position: Int) {
        titleManager.onTitleItemActionIconClicked(position)
    }

    override val onCreateNewChecklistItemClicked: (position: Int) -> Unit =
        checklistManager.onCreateNewChecklistItemClicked

    override var onItemDeleted: (text: String, id: Long) -> Unit
        get() = checklistManager.onItemDeleted
        set(value) {
            checklistManager.onItemDeleted = value
        }

    override fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistManagerConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit,
        enableItemAnimations: (isEnabled: Boolean) -> Unit
    ) {
        checklistManager.lateInitState(
            adapter,
            config,
            scrollToPosition,
            startDragAndDrop,
            enableDragAndDrop,
            updateItemPadding,
            enableItemAnimations
        )
    }

    override fun setItems(formattedText: String) {
        checklistManager.setItems(formattedText)
    }

    override fun getFormattedTextItems(
        keepCheckboxSymbols: Boolean,
        keepCheckedItems: Boolean
    ): String {
        return checklistManager.getFormattedTextItems(
            keepCheckboxSymbols,
            keepCheckedItems
        )
    }

    override fun setConfig(config: ChecklistManagerConfig) {
        checklistManager.setConfig(config)
    }

    override fun restoreDeleteItems(itemIds: List<Long>): Boolean {
        return checklistManager.restoreDeleteItems(itemIds)
    }

    override fun restoreDeletedItem(itemId: Long): Boolean {
        return checklistManager.restoreDeletedItem(itemId)
    }

    override fun removeAllCheckedItems(): List<Long> {
        return checklistManager.removeAllCheckedItems()
    }

    override fun uncheckAllCheckedItems(): Boolean {
        return checklistManager.uncheckAllCheckedItems()
    }

    override fun getItemCount(): Int {
        return checklistManager.getItemCount()
    }

    override fun getCheckedItemCount(): Int {
        return checklistManager.getCheckedItemCount()
    }

    override fun getChecklistItemAtPosition(position: Int): ChecklistItem? {
        return checklistManager.getChecklistItemAtPosition(position)
    }

    override fun updateChecklistItem(item: ChecklistItem): Boolean {
        return checklistManager.updateChecklistItem(item)
    }

    override fun onItemChecked(
        position: Int,
        isChecked: Boolean
    ) {
        checklistManager.onItemChecked(
            position,
            isChecked
        )
    }

    override fun onItemTextChanged(
        position: Int,
        text: String
    ) {
        checklistManager.onItemTextChanged(
            position,
            text
        )
    }

    override fun onItemEnterKeyPressed(
        position: Int,
        textView: TextView
    ) {
        checklistManager.onItemEnterKeyPressed(
            position,
            textView
        )
    }

    override fun onItemDeleteKeyPressed(position: Int) {
        checklistManager.onItemDeleteKeyPressed(position)
    }

    override fun onItemDeleteClicked(position: Int) {
        checklistManager.onItemDeleteClicked(position)
    }

    override fun onItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        checklistManager.onItemFocusChanged(
            position,
            startSelection,
            endSelection,
            hasFocus
        )
    }

    override fun onItemSelectionChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        checklistManager.onItemSelectionChanged(
            position,
            startSelection,
            endSelection,
            hasFocus
        )
    }

    override fun onItemDragHandledClicked(position: Int) {
        checklistManager.onItemDragHandledClicked(position)
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

    override fun getItemFocusPosition(): Int {
        return checklistManager.getItemFocusPosition()
    }

    override fun setItemFocusPosition(position: Int): Boolean {
        return checklistManager.setItemFocusPosition(position)
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
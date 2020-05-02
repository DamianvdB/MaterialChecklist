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

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem

private const val NO_POSITION: Int = -1

/**
 * Manages the focus and the selection behavior of the [checklistItems]. Checklist items
 * will have their focus set and removed using [requestFocus] in combination of calling
 * [hideKeyboard] based on different behavioural rules. All the unchecked items should always be
 * above the [createNewItemPosition] and all the checked items should always be below [createNewItemPosition].
 */
internal class ChecklistFocusManagerImpl(
    private val checklistItems: () -> List<BaseRecyclerItem>,
    private val requestFocus: (position: Int, selectionPosition: Int, showKeyboard: Boolean) -> Unit,
    private val hideKeyboard: () -> Unit,
    private val createNewItemPosition: () -> Int
) : ChecklistFocusManager {
    private var itemFocusTracker: ItemFocusTracker = ItemFocusTracker()
    private var itemFocusTrackerPreCheckedStateChanged: ItemFocusTracker = ItemFocusTracker()

    /**
     * Called when a checklist item's [hasFocus] changed with [startSelection] in the edit text
     * for the item at [position] in the list.
     */
    override fun onItemFocusChanged(
        position: Int,
        startSelection: Int,
        hasFocus: Boolean
    ) {
        if (hasFocus) {
            itemFocusTracker = ItemFocusTracker(
                position,
                startSelection
            )
        }
    }

    /**
     * Called when a checklist item's [startSelection] in the edit text has changed
     * for item at [position] in the list.
     */
    override fun onItemSelectionChanged(
        position: Int,
        startSelection: Int,
        hasFocus: Boolean
    ) {
        if (hasFocus) {
            itemFocusTracker = ItemFocusTracker(
                position,
                startSelection
            )
        }
    }

    /**
     * Called when checklist [updatedItems] are set which replaces the checklist [originalItems].
     */
    override fun onItemsSet(
        originalItems: List<BaseRecyclerItem>,
        updatedItems: List<BaseRecyclerItem>
    ) {
        resetState()

        if (originalItems.isEmpty() && updatedItems.isNotEmpty()) {
            var currentPosition = 0
            do {
                if (currentPosition != createNewItemPosition()) {
                    requestFocusForItemAtPosition(currentPosition)
                    break
                }

            } while (++currentPosition < updatedItems.lastIndex)
        }
    }

    /**
     * Called when a new checklist item is created at [position] in the list.
     */
    override fun onNewItemCreated(position: Int) {
        requestFocusForItemAtPosition(
            position = position,
            isShowKeyboard = true
        )
    }

    /**
     * Called before a checklist item at [position] has it's checked state changed.
     *
     * Store positional information about the checklist item at [position] before its
     * position in the list changes as a result of checking/unchecking the item.
     */
    override fun onItemPreCheckedStateChanged(position: Int) {
        if (position == itemFocusTracker.position) {
            itemFocusTrackerPreCheckedStateChanged = ItemFocusTracker(
                position = itemFocusTracker.position,
                selectionPosition = itemFocusTracker.selectionPosition
            )
        }
    }

    /**
     * Called when a checklist [item] has been checked, causing the item to
     * move from [originalPosition] to [updatedPosition] in the list. If the item's [updatedPosition]
     * is [NO_POSITION], this item is considered to have been removed from the list.
     */
    override fun onItemChecked(
        originalPosition: Int,
        updatedPosition: Int,
        item: ChecklistRecyclerItem
    ) {
        handleItemCheckedStateChanged(
            originalPosition,
            updatedPosition,
            item
        )
    }

    /**
     * Called when a checklist [item] has been unchecked, causing the item to
     * move from [originalPosition] to [updatedPosition] in the list. If the item's [updatedPosition]
     * is [NO_POSITION], this item is considered to have been removed from the list.
     */
    override fun onItemUnchecked(
        originalPosition: Int,
        updatedPosition: Int,
        item: ChecklistRecyclerItem
    ) {
        handleItemCheckedStateChanged(
            originalPosition,
            updatedPosition,
            item
        )
    }

    /**
     * Called when a checklist [item] at [position] in the list has been deleted. The
     * [isDeletedIconClicked] will be true if the item has been deleted as a result of clicking
     * on the delete icon.
     */
    override fun onItemDeleted(
        position: Int,
        item: ChecklistRecyclerItem,
        isDeletedIconClicked: Boolean
    ) {
        val requestFocusPosition = if (isDeletedIconClicked) position else position.dec()

        findNextItemPositionForFocusOnItemDeleted(
            position = requestFocusPosition,
            deletedItem = item
        )
    }

    /**
     * Called when all checklist items that marked as checked have been removed from [itemPositions]
     * in the list.
     */
    override fun onAllCheckedItemsRemoved(itemPositions: List<Int>) {
        val removedItemHadFocus = itemPositions.any { it == itemFocusTracker.position }

        if (removedItemHadFocus) {
            hideKeyboardAndResetState()
        } else {
            // Request focus to maintain selection position for focused checklist item
            if (itemFocusTracker.selectionPosition != NO_POSITION) {
                requestFocusForItemAtPosition(
                    position = itemFocusTracker.position,
                    selectionPosition = itemFocusTracker.selectionPosition
                )
            }
        }
    }

    /**
     * Called when drag-and-drop functionality has been started for a checklist item.
     */
    override fun onItemDragStarted() {
        hideKeyboardAndResetState()
    }

    private fun handleItemCheckedStateChanged(
        originalPosition: Int,
        updatedPosition: Int,
        item: ChecklistRecyclerItem
    ) {
        val itemRemovedOnCheckedStateChanged = updatedPosition == NO_POSITION
        val itemHadFocusPreCheckedStateChanged = originalPosition == itemFocusTrackerPreCheckedStateChanged.position

        if (!itemRemovedOnCheckedStateChanged) {
            if (itemHadFocusPreCheckedStateChanged) {
                requestFocusForItemAtPosition(
                    position = updatedPosition,
                    selectionPosition = itemFocusTrackerPreCheckedStateChanged.selectionPosition
                )
            }
        } else if (itemHadFocusPreCheckedStateChanged) {
            findNextItemPositionForFocusOnItemDeleted(
                position = originalPosition,
                deletedItem = item
            )
        }

        resetItemFocusTrackerPreCheckedStateChanged()
    }

    private fun findNextItemPositionForFocusOnItemDeleted(
        position: Int,
        deletedItem: ChecklistRecyclerItem
    ) {
        if (position != createNewItemPosition() && position < checklistItems().size) {
            requestFocusForItemAtPosition(
                position = position,
                selectionPosition = Int.MAX_VALUE
            )
        } else {
            val isWithinRange: (Int) -> Boolean = {
                it in (position.dec())..(position.inc())
            }
            val validPositionMap = checklistItems()
                .filterIndexed { index, item ->
                    (item as? ChecklistRecyclerItem)?.isChecked == deletedItem.isChecked && isWithinRange(index)
                }.map {
                    checklistItems().indexOf(it)
                }

            if (validPositionMap.isEmpty()) {    // No items with the same 'isChecked' state
                hideKeyboardAndResetState()
            } else {
                requestFocusForItemAtPosition(
                    position = validPositionMap.first(),
                    selectionPosition = Int.MAX_VALUE
                )
            }
        }
    }

    private fun hideKeyboardAndResetState() {
        hideKeyboard()
        resetState()
    }

    private fun resetState() {
        itemFocusTracker = ItemFocusTracker()
        resetItemFocusTrackerPreCheckedStateChanged()
        requestFocusForItemAtPosition(NO_POSITION)
    }

    private fun resetItemFocusTrackerPreCheckedStateChanged() {
        itemFocusTrackerPreCheckedStateChanged = ItemFocusTracker()
    }

    private fun requestFocusForItemAtPosition(
        position: Int,
        selectionPosition: Int = 0,
        isShowKeyboard: Boolean = false
    ) {
        requestFocus(
            position,
            selectionPosition,
            isShowKeyboard
        )
    }

    /**
     * A class that holds the edit text [selectionPosition] for a checklist item
     * with focus at [position] in the list.
     */
    private data class ItemFocusTracker(
        val position: Int = NO_POSITION,
        val selectionPosition: Int = NO_POSITION
    )
}
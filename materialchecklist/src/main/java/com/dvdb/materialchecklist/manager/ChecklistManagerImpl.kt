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
import com.dvdb.materialchecklist.config.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.DragAndDropDismissKeyboardBehavior
import com.dvdb.materialchecklist.manager.config.ChecklistManagerConfig
import com.dvdb.materialchecklist.manager.item.ChecklistItem
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.util.DefaultRecyclerItemComparator
import com.dvdb.materialchecklist.recycler.util.RecyclerItemMapper
import com.dvdb.materialchecklist.util.DelayHandler
import java.util.*

private const val NO_POSITION = -1
private const val ENABLE_ITEM_ANIMATIONS_DELAY_MS = 1000L
private const val MIN_ITEM_COUNT = 2

/**
 * Manages the state and behavior of the checklist items.
 */
internal class ChecklistManagerImpl(
    hideKeyboard: () -> Unit
) : ChecklistManager {

    /**
     * Called when the [ChecklistNewRecyclerItem] is clicked at
     * position in the list.
     */
    override val onCreateNewChecklistItemClicked: (position: Int) -> Unit = { position ->
        updateItemInAdapter(
            ChecklistRecyclerItem(""),
            position
        )

        focusManager.onNewItemCreated(position)

        addItemToAdapter(
            ChecklistNewRecyclerItem(),
            position.inc()
        )
    }

    /**
     * Called when a checklist item has been deleted. The id of the item can
     * be used to restore the deleted item by calling [restoreDeletedItem].
     */
    override var onItemDeleted: ((text: String, id: Long) -> Unit) = { _, _ -> }

    private val focusManager: ChecklistFocusManager =
        ChecklistFocusManagerImpl(
            checklistItems = { if (::adapter.isInitialized) adapter.items else emptyList() },
            requestFocus = createFocusManagerRequestFocusAtPositionInAdapterFunction(),
            hideKeyboard = hideKeyboard,
            createNewItemPosition = { createNewItemPosition },
            dragAndDropDismissKeyboardBehavior = {
                if (::config.isInitialized) {
                    config.dragAndDropDismissKeyboardBehavior
                } else {
                    DragAndDropDismissKeyboardBehavior.DEFAULT
                }
            }
        )

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: ChecklistManagerConfig
    private lateinit var scrollToPosition: (position: Int) -> Unit
    private lateinit var startDragAndDrop: (position: Int) -> Unit
    private lateinit var enableDragAndDrop: (isEnabled: Boolean) -> Unit
    private lateinit var updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit
    private lateinit var enableItemAnimations: (isEnabled: Boolean) -> Unit

    /**
     * The [createNewItemPosition] of the [ChecklistNewRecyclerItem] in the list. All checked
     * [ChecklistRecyclerItem]s should always be positioned above [createNewItemPosition]. All unchecked
     * [ChecklistRecyclerItem]s should always be positioned below [createNewItemPosition].
     */
    private val createNewItemPosition: Int
        get() = adapter.items.indexOfFirst { it is ChecklistNewRecyclerItem }

    private val delayHandler: DelayHandler = DelayHandler()

    private val previousUncheckedItemPositions: MutableMap<Long, Int> = mutableMapOf()
    private val deletedItems: MutableMap<Long, Pair<ChecklistRecyclerItem, Int>> = mutableMapOf()

    /**
     * Initialise the checklist manager with the required functions.
     *
     * This method should only be called once!
     *
     * @param adapter The adapter for the checklist items.
     * @param config The configuration defining the behavior and appearance of checklist items.
     * @param scrollToPosition The function to scroll to the provided position of a checklist item in the list.
     * @param startDragAndDrop The function to start the drag-and-drop functionality for a
     * checklist item at the provided position in the list.
     * @param enableDragAndDrop The function to enable or disable the drag-and-drop functionality.
     * @param updateItemPadding The function to update the padding of the first and/or last checklist item in the list.
     * @param enableItemAnimations The function to enable or disable the list animations on data set changes.
     */
    override fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistManagerConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit,
        enableItemAnimations: (isEnabled: Boolean) -> Unit
    ) {
        if (this::adapter.isInitialized) {
            error("Checklist manager state should only be initialised once")
        }

        this.adapter = adapter
        this.config = config
        this.scrollToPosition = scrollToPosition
        this.startDragAndDrop = startDragAndDrop
        this.enableDragAndDrop = enableDragAndDrop.also { it(config.dragAndDropEnabled) }
        this.updateItemPadding =
            updateItemPadding.also { it(config.itemFirstTopPadding, config.itemLastBottomPadding) }
        this.enableItemAnimations = enableItemAnimations
    }

    /**
     * Set and displays the checklist items by parsing the
     * [formattedText] into checklist items.
     */
    override fun setItems(formattedText: String) {
        setItemsInternal(RecyclerItemMapper.toItems(formattedText))
    }

    /**
     * Get the formatted text representation of the current
     * checklist items. The [keepCheckboxSymbols] flag is used to
     * either retain or remove the checkbox symbols of the checklist items.
     * The [keepCheckedItems] flag is used to either retain or remove all the checked
     * checklist items.
     */
    override fun getFormattedTextItems(
        keepCheckboxSymbols: Boolean,
        keepCheckedItems: Boolean
    ): String {
        return RecyclerItemMapper.toFormattedText(
            items = adapter.items.filterIsInstance<ChecklistRecyclerItem>(),
            keepCheckboxSymbols = keepCheckboxSymbols,
            keepCheckedItems = keepCheckedItems
        )
    }

    /**
     * Set the customisable [config] which defines the appearance and
     * behavior of the checklist items.
     */
    override fun setConfig(config: ChecklistManagerConfig) {
        if (this.config.dragAndDropEnabled != config.dragAndDropEnabled) {
            enableDragAndDrop(config.dragAndDropEnabled)
        }

        if (this.config.itemFirstTopPadding != config.itemFirstTopPadding || this.config.itemLastBottomPadding != config.itemLastBottomPadding) {
            updateItemPadding(config.itemFirstTopPadding, config.itemLastBottomPadding)
        }

        if (this.config.adapterConfig != config.adapterConfig) {
            adapter.config = config.adapterConfig
        }

        this.config = config
    }

    /**
     * Restore the checklist items with [itemIds] that were deleted in the
     * current session. Return true if all the items with [itemIds] were
     * restored.
     */
    override fun restoreDeleteItems(itemIds: List<Long>): Boolean {
        var allItemsRestore = true

        itemIds.forEach { itemId ->
            if (!restoreDeletedItem(itemId) && allItemsRestore) {
                allItemsRestore = false
            }
        }

        return allItemsRestore
    }

    /**
     * Restore the checklist item with [itemId] that was deleted in the
     * current session. Return true if the item with [itemId] was
     * restored.
     */
    override fun restoreDeletedItem(itemId: Long): Boolean {
        deletedItems.remove(itemId)?.let { (item, position) ->
            val addItemPosition = if (item.isChecked) {
                position.coerceIn(createNewItemPosition.inc(), adapter.itemCount)
            } else {
                position.coerceIn(0, createNewItemPosition)
            }

            addItemToAdapter(
                item,
                addItemPosition
            )

            if (config.itemLastBottomPadding != 0f && adapter.items.lastIndex == addItemPosition) {
                adapter.notifyItemChanged(adapter.items.lastIndex - 1)
            }

            return true
        }
        return false
    }

    /**
     * Remove all checklist items that are marked as checked. The
     * item ids of all the removed items will be returned.
     */
    override fun removeAllCheckedItems(): List<Long> {
        val removedItemIds: MutableList<Long> = mutableListOf()
        val removedItemPositions: MutableList<Int> = mutableListOf()
        val items = adapter.items.filterIndexed { index, item ->
            val shouldKeep = item is ChecklistNewRecyclerItem ||
                    item is ChecklistRecyclerItem &&
                    !item.isChecked
            if (!shouldKeep) {
                removedItemIds.add((item as ChecklistRecyclerItem).id)
                removedItemPositions.add(index)

                saveDeletedItem(
                    item,
                    index
                )
            }

            shouldKeep
        }

        if (removedItemIds.isNotEmpty()) {
            setItemsInAdapter(items)

            handleMinimumItemCountInAdapter()
        }

        focusManager.onAllCheckedItemsRemoved(removedItemPositions)

        return removedItemIds
    }

    /**
     * Uncheck all the checklist items that are marked as checked. Return true
     * if any checklist items were marked as unchecked.
     */
    override fun uncheckAllCheckedItems(): Boolean {
        var anyItemsUnchecked = false

        adapter.items.forEachIndexed { index, item ->
            if (item is ChecklistRecyclerItem && item.isChecked) {
                handleItemUnchecked(
                    item,
                    index
                )

                anyItemsUnchecked = true
            }
        }

        return anyItemsUnchecked
    }

    /**
     * Get the total number of checklist items.
     */
    override fun getItemCount(): Int {
        return adapter.itemCount
    }

    /**
     * Get the total number of checklist items that
     * are marked as checked.
     */
    override fun getCheckedItemCount(): Int {
        return adapter.items.count { it is ChecklistRecyclerItem && it.isChecked }
    }

    /**
     * Get the checklist item at [position] in the list or null if no item
     * could not be found at [position].
     */
    override fun getChecklistItemAtPosition(position: Int): ChecklistItem? {
        return (adapter.items.getOrNull(position) as? ChecklistRecyclerItem)?.toChecklistItem()
    }

    /**
     * Update the checklist [item] in the list with same id.
     *
     * Return 'true' if a checklist item with the same id could be found
     * and it has different values when compared to [item]. Otherwise, return 'false'
     * if a checklist item could not be found or they have same values.
     */
    override fun updateChecklistItem(item: ChecklistItem): Boolean {
        var isItemUpdated = false
        val currentItem = adapter.items.firstOrNull { it.id == item.id }

        if (currentItem is ChecklistRecyclerItem) {
            val newItem = ChecklistRecyclerItem(
                text = item.text,
                isChecked = item.isChecked,
                id = item.id
            )

            if (currentItem != newItem) {
                val position = adapter.items.indexOf(currentItem)

                if (position != NO_POSITION) {

                    if (currentItem.isChecked != newItem.isChecked) {
                        val newItemWithCurrentIsChecked =
                            newItem.copy(isChecked = currentItem.isChecked)

                        if (newItem.isChecked) {
                            handleItemChecked(
                                item = newItemWithCurrentIsChecked,
                                position = position
                            )
                        } else {
                            handleItemUnchecked(
                                item = newItemWithCurrentIsChecked,
                                position = position
                            )
                        }
                    } else {
                        updateItemInAdapter(
                            item = newItem,
                            position = position
                        )

                        focusManager.onItemUpdated(
                            position = position,
                            item = newItem
                        )
                    }

                    isItemUpdated = true
                }
            }
        }
        return isItemUpdated
    }

    /**
     * Called when a checklist item at [position] in the list
     * has been marked as checked or unchecked, depending on the value of
     * [isChecked].
     */
    override fun onItemChecked(
        position: Int,
        isChecked: Boolean
    ) {
        val item = adapter.items.getOrNull(position)

        if (item is ChecklistRecyclerItem) {
            if (isChecked) {
                handleItemChecked(item, position)
            } else {
                handleItemUnchecked(item, position)
            }
        }
    }

    /**
     * Called when the [text] of a checklist item at [position] in the list
     * has changed.
     */
    override fun onItemTextChanged(
        position: Int,
        text: String
    ) {
        val item = adapter.items.getOrNull(position)

        if (item is ChecklistRecyclerItem) {
            updateItemInAdapter(
                item.copy(text = text),
                position,
                false
            )
        }
    }

    /**
     * Called when the enter key of the [textView] has been pressed for a checklist
     * item at [position] in the list.
     */
    override fun onItemEnterKeyPressed(
        position: Int,
        textView: TextView
    ) {
        val currentItem = adapter.items.getOrNull(position)

        if (currentItem is ChecklistRecyclerItem) {
            val text = textView.text
            val textLength = text.length
            val hasSelection = textView.hasSelection()
            val selectionStart = textView.selectionStart
            val selectionEnd = textView.selectionEnd

            val currentItemText = if (hasSelection) {
                text.substring(0, selectionStart) + text.substring(selectionEnd, textLength)
            } else {
                text.substring(0, selectionStart)
            }

            val newItemText = if (hasSelection) {
                text.substring(selectionStart, selectionEnd)
            } else {
                text.substring(selectionEnd, textLength)
            }

            updateItemInAdapter(
                currentItem.copy(text = currentItemText),
                position
            )

            val newItemPosition = position + 1
            addItemToAdapter(
                ChecklistRecyclerItem(
                    text = newItemText,
                    isChecked = currentItem.isChecked
                ),
                newItemPosition
            )

            focusManager.onNewItemCreated(newItemPosition)
        }
    }

    /**
     * Called when the delete key has been pressed for a checklist
     * item at [position] in the list.
     */
    override fun onItemDeleteKeyPressed(position: Int) {
        handleDeleteItem(
            position = position,
            saveDeletedItemForRestoration = false,
            deletedIconClicked = false
        )
    }

    /**
     * Called when the delete icon has been clicked for a checklist item
     * at [position] in the list.
     */
    override fun onItemDeleteClicked(position: Int) {
        handleDeleteItem(position)
    }

    /**
     * Called when a checklist item's [hasFocus] changed with [startSelection] in the edit text
     * for the item at [position] in the list.
     */
    override fun onItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        focusManager.onItemFocusChanged(
            position = position,
            startSelection = startSelection,
            hasFocus = hasFocus
        )
    }

    /**
     * Called when a checklist item's [startSelection] in the edit text has changed
     * for item at [position] in the list.
     */
    override fun onItemSelectionChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        focusManager.onItemSelectionChanged(
            position = position,
            startSelection = startSelection,
            hasFocus = hasFocus
        )
    }

    /**
     * Called when the drag handle icon has been clicked for a checklist item
     * at [position] in the list.
     */
    override fun onItemDragHandledClicked(position: Int) {
        startDragAndDrop(position)
    }

    /**
     * Called when a checklist item has been moved from [fromPosition] to [toPosition]
     * in the list as a result of dragging.
     */
    override fun onItemMove(
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        val items: MutableList<BaseRecyclerItem> = adapter.items.toMutableList()

        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(items, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(items, i, i - 1)
            }
        }

        notifyItemMovedInAdapter(
            fromPosition,
            toPosition
        )

        setItemsInAdapter(
            items = items,
            notify = false
        )

        focusManager.onItemDragPositionChanged(
            fromPosition,
            toPosition
        )

        return true
    }

    /**
     * Determines if the checklist item at [currentPosition] can be
     * dropped over the target item at [targetPosition] in the list.
     * Returns true if the item at [targetPosition] is a [ChecklistRecyclerItem]
     * and is marked as unchecked.
     */
    override fun canDragOverTargetItem(
        currentPosition: Int,
        targetPosition: Int
    ): Boolean {
        val currentChecklistItem =
            adapter.items.getOrNull(currentPosition) as? ChecklistRecyclerItem
        val targetChecklistItem = adapter.items.getOrNull(targetPosition) as? ChecklistRecyclerItem

        return currentChecklistItem?.isChecked == false && targetChecklistItem?.isChecked == false
    }

    /**
     * Called when the drag-and-drop functionality of a
     * checklist item has started.
     */
    override fun onItemDragStarted(position: Int) {
        focusManager.onItemDragStarted(position)
    }

    /**
     * Get the position of the checklist item in the list
     * that has focus using the [focusManager].
     */
    override fun getItemFocusPosition(): Int = focusManager.getItemFocusPosition()

    /**
     * Set the focus on the checklist item at [position]
     * in the list using the [focusManager].
     *
     * Returns 'true' if focus could be set on a
     * checklist item.
     */
    override fun setItemFocusPosition(position: Int): Boolean {
        return focusManager.setItemFocusPosition(position)
    }

    private fun setItemsInternal(items: List<BaseRecyclerItem>) {
        resetState()

        val sortedItems: List<BaseRecyclerItem> = if (items.isNotEmpty()) {
            items.filterIsInstance<ChecklistRecyclerItem>()
        } else {
            listOf(ChecklistRecyclerItem(""))
        }.plus(ChecklistNewRecyclerItem())
            .sortedWith(DefaultRecyclerItemComparator)

        enableItemAnimations(false)

        setItemsInAdapter(sortedItems)

        delayHandler.run(ENABLE_ITEM_ANIMATIONS_DELAY_MS) {
            enableItemAnimations(true)
        }
    }

    private fun handleItemChecked(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        focusManager.onItemPreCheckedStateChanged(position)
        removeItemFromAdapter(position)

        val toPosition: Int =
            when (config.behaviorCheckedItem) {
                BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS -> {
                    val firstCheckedItemPosition =
                        adapter.items.indexOfFirst { it is ChecklistRecyclerItem && it.isChecked }
                    when {
                        firstCheckedItemPosition != NO_POSITION -> firstCheckedItemPosition
                        createNewItemPosition != NO_POSITION -> createNewItemPosition.inc()
                        else -> adapter.items.lastIndex
                    }.coerceIn(0, adapter.itemCount)
                }

                BehaviorCheckedItem.MOVE_TO_BOTTOM_OF_CHECKED_ITEMS -> adapter.itemCount

                BehaviorCheckedItem.DELETE -> {
                    saveDeletedItemAndNotifyListener(item, position)
                    NO_POSITION
                }
            }

        if (toPosition != NO_POSITION) {
            addItemToAdapter(
                item.copy(isChecked = true),
                toPosition
            )

            if (config.behaviorUncheckedItem == BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION) {
                previousUncheckedItemPositions[item.id] = position
            }
        } else {
            handleMinimumItemCountInAdapter()
        }

        focusManager.onItemChecked(
            originalPosition = position,
            updatedPosition = toPosition,
            item = item
        )
    }

    private fun handleItemUnchecked(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        focusManager.onItemPreCheckedStateChanged(position)
        removeItemFromAdapter(position)

        val newItemPosition = createNewItemPosition.coerceAtLeast(0)
        val toPosition: Int =
            when (config.behaviorUncheckedItem) {
                BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION -> {
                    previousUncheckedItemPositions.remove(item.id)?.coerceAtMost(newItemPosition)
                        ?: newItemPosition
                }
                BehaviorUncheckedItem.MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS -> newItemPosition
                BehaviorUncheckedItem.MOVE_TO_TOP_OF_UNCHECKED_ITEMS -> 0
            }

        addItemToAdapter(
            item.copy(isChecked = false),
            toPosition
        )

        focusManager.onItemUnchecked(
            originalPosition = position,
            updatedPosition = toPosition,
            item = item
        )
    }

    private fun handleDeleteItem(
        position: Int,
        saveDeletedItemForRestoration: Boolean = true,
        deletedIconClicked: Boolean = true
    ) {
        val itemToDelete = adapter.items.getOrNull(position)

        if (itemToDelete is ChecklistRecyclerItem) {
            removeItemFromAdapter(position)

            if (saveDeletedItemForRestoration) {
                saveDeletedItemAndNotifyListener(itemToDelete, position)
            }

            if (!handleMinimumItemCountInAdapter()) {
                focusManager.onItemDeleted(
                    position = position,
                    item = itemToDelete,
                    isDeletedIconClicked = deletedIconClicked
                )
            }
        }
    }

    private fun handleMinimumItemCountInAdapter(): Boolean {
        if (adapter.itemCount < MIN_ITEM_COUNT) {
            val itemInsertionPosition = 0

            addItemToAdapter(
                ChecklistRecyclerItem(""),
                itemInsertionPosition
            )

            focusManager.onNewItemCreated(itemInsertionPosition)
            return true
        }

        return false
    }

    private fun createFocusManagerRequestFocusAtPositionInAdapterFunction(): (position: Int, selectionPosition: Int, showKeyboard: Boolean) -> Unit {
        return { position, selectionPosition, showKeyboard ->
            scrollToPosition(position)

            delayHandler.run {
                adapter.requestFocus = ChecklistItemAdapterRequestFocus(
                    position = position,
                    selectionPosition = selectionPosition,
                    isShowKeyboard = showKeyboard
                )
            }
        }
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

    private fun notifyItemMovedInAdapter(
        fromPosition: Int,
        toPosition: Int
    ) {
        adapter.notifyItemMoved(
            fromPosition,
            toPosition
        )
    }

    private fun saveDeletedItem(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        deletedItems[item.id] = Pair(
            item,
            position
        )
    }

    private fun saveDeletedItemAndNotifyListener(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        saveDeletedItem(
            item,
            position
        )

        onItemDeleted.invoke(
            item.text,
            item.id
        )
    }

    private fun resetState() {
        previousUncheckedItemPositions.clear()
        deletedItems.clear()
    }
}
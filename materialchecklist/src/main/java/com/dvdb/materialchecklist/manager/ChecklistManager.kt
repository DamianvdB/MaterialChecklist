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
import com.dvdb.materialchecklist.manager.config.ChecklistManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapterDragListener
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapterRequestFocus
import com.dvdb.materialchecklist.recycler.holder.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.util.DefaultRecyclerItemComparator
import com.dvdb.materialchecklist.recycler.util.RecyclerItemMapper
import com.dvdb.materialchecklist.util.DelayHandler
import java.util.*

private const val NO_POSITION = -1

internal class ChecklistManager(
    private val hideKeyboard: () -> Unit
) : ChecklistRecyclerHolderItemListener,
    ChecklistItemAdapterDragListener {

    val onNewChecklistListItemClicked: (position: Int) -> Unit = { position ->
        updateItemInAdapter(
            ChecklistRecyclerItem(""),
            position
        )

        requestFocusForPositionInAdapter(
            position,
            isShowKeyboard = true
        )

        addItemToAdapter(
            ChecklistNewRecyclerItem(),
            position + 1
        )
    }

    var onItemDeleted: ((text: String, id: Long) -> Unit)? = null

    private lateinit var adapter: ChecklistItemAdapter
    private lateinit var config: ChecklistManagerConfig
    private lateinit var scrollToPosition: (position: Int) -> Unit
    private lateinit var startDragAndDrop: (position: Int) -> Unit
    private lateinit var enableDragAndDrop: (isEnabled: Boolean) -> Unit
    private lateinit var updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit

    private val newItemPosition: Int
        get() = adapter.items.indexOfFirst { it is ChecklistNewRecyclerItem }

    private val delayHandler: DelayHandler = DelayHandler()

    private var currentPosition: Int = NO_POSITION

    private val previousUncheckedItemPositions: MutableMap<Long, Int> = mutableMapOf()

    private val deletedItems: MutableMap<Long, Pair<ChecklistRecyclerItem, Int>> = mutableMapOf()

    fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistManagerConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit
    ) {
        if (this::adapter.isInitialized) {
            error("Checklist manager state should only be initialised once")
        }

        this.adapter = adapter
        this.config = config
        this.scrollToPosition = scrollToPosition
        this.startDragAndDrop = startDragAndDrop
        this.enableDragAndDrop = enableDragAndDrop.also { it(config.dragAndDropEnabled) }
        this.updateItemPadding = updateItemPadding.also { it(config.itemFirstTopPadding, config.itemLastBottomPadding) }
    }

    fun setItems(formattedText: String) {
        setItemsInternal(RecyclerItemMapper.toItems(formattedText))
    }

    fun getFormattedTextItems(
        keepCheckedItems: Boolean,
        skipCheckedItems: Boolean
    ): String {
        return RecyclerItemMapper.toFormattedText(
            items = adapter.items.filterIsInstance<ChecklistRecyclerItem>(),
            keepCheckSymbols = keepCheckedItems,
            skipCheckedItems = skipCheckedItems
        )
    }

    fun setConfig(config: ChecklistManagerConfig) {
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

    fun restoreDeleteItems(itemIds: List<Long>): Boolean {
        var allItemsRemoved = true

        itemIds.forEach { itemId ->
            if (!restoreDeletedItem(itemId) && allItemsRemoved) {
                allItemsRemoved = false
            }
        }

        return allItemsRemoved
    }

    fun restoreDeletedItem(itemId: Long): Boolean {
        deletedItems.remove(itemId)?.let { (item, position) ->
            val addItemPosition = if (item.isChecked) {
                position.coerceIn(newItemPosition, adapter.itemCount)
            } else {
                position.coerceIn(0, newItemPosition)
            }

            addItemToAdapter(
                item,
                addItemPosition
            )

            return true
        }
        return false
    }

    fun removeAllCheckedItems(): List<Long> {
        val removedItemIds: MutableList<Long> = mutableListOf()
        val items = adapter.items.filterIndexed { index, item ->
            val shouldKeep = item is ChecklistNewRecyclerItem || item is ChecklistRecyclerItem && !item.isChecked
            if (!shouldKeep) {
                removedItemIds.add((item as ChecklistRecyclerItem).id)

                saveDeletedItem(
                    item,
                    index
                )
            }

            shouldKeep
        }

        if (removedItemIds.isNotEmpty()) {
            adapter.setItems(items)
        }

        return removedItemIds
    }

    fun uncheckAllCheckedItems(): Boolean {
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

            requestFocusForPositionInAdapter(
                newItemPosition,
                isStartSelection = true
            )
        }
    }

    override fun onItemDeleteClicked(position: Int) {
        handleDeleteItem(position)
    }

    override fun onItemDeleteKeyPressed(position: Int) {
        handleDeleteItem(position, false)
    }

    override fun onItemFocusChanged(
        position: Int,
        hasFocus: Boolean
    ) {
        if (hasFocus) {
            currentPosition = position
        }
    }

    override fun onItemDragHandledClicked(position: Int) {
        startDragAndDrop(position)
    }

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

        return true
    }

    override fun canDragOverTargetItem(
        currentPosition: Int,
        targetPosition: Int
    ): Boolean {
        val currentChecklistItem = adapter.items.getOrNull(currentPosition) as? ChecklistRecyclerItem
        val targetChecklistItem = adapter.items.getOrNull(targetPosition) as? ChecklistRecyclerItem

        return currentChecklistItem?.isChecked == false && targetChecklistItem?.isChecked == false
    }

    override fun onItemDragStart() {
        hideKeyboard()
    }

    private fun setItemsInternal(items: List<BaseRecyclerItem>) {
        val sortedItems: List<BaseRecyclerItem> = if (items.isNotEmpty()) {
            items.filterIsInstance<ChecklistRecyclerItem>()
        } else {
            currentPosition = 0
            listOf(ChecklistRecyclerItem(""))
        }.plus(ChecklistNewRecyclerItem())
            .sortedWith(DefaultRecyclerItemComparator)

        setItemsInAdapter(sortedItems)

        if (currentPosition != NO_POSITION) {
            requestFocusForPositionInAdapter(currentPosition)
        }
    }

    private fun handleItemChecked(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        removeItemFromAdapter(position)

        val toPosition: Int =
            when (config.behaviorCheckedItem) {
                BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS -> {
                    val firstCheckedItemPosition = adapter.items.indexOfFirst { it is ChecklistRecyclerItem && it.isChecked }
                    when {
                        firstCheckedItemPosition != NO_POSITION -> firstCheckedItemPosition
                        newItemPosition != NO_POSITION -> newItemPosition.inc()
                        else -> adapter.items.lastIndex
                    }.coerceIn(0, adapter.itemCount)
                }

                BehaviorCheckedItem.MOVE_TO_BOTTOM_OF_CHECKED_ITEMS -> adapter.itemCount

                BehaviorCheckedItem.KEEP_POSITION -> position

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

            if (position == currentPosition) {
                requestFocusForPositionInAdapter(toPosition)
            }

            if (config.behaviorUncheckedItem == BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION) {
                previousUncheckedItemPositions[item.id] = position
            }
        }
    }

    private fun handleItemUnchecked(
        item: ChecklistRecyclerItem,
        position: Int
    ) {
        removeItemFromAdapter(position)

        val newItemPosition = newItemPosition.coerceAtLeast(0)
        val toPosition: Int =
            when (config.behaviorUncheckedItem) {
                BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION -> {
                    previousUncheckedItemPositions.remove(item.id)?.coerceAtMost(newItemPosition) ?: newItemPosition
                }
                BehaviorUncheckedItem.MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS -> newItemPosition
                BehaviorUncheckedItem.MOVE_TO_TOP_OF_UNCHECKED_ITEMS -> 0
            }

        addItemToAdapter(
            item.copy(isChecked = false),
            toPosition
        )

        if (position == currentPosition) {
            requestFocusForPositionInAdapter(toPosition)
        }
    }

    private fun handleDeleteItem(position: Int, savedDeletedItemForRestoration: Boolean = true) {
        val itemToDelete = adapter.items.getOrNull(position)

        if (itemToDelete is ChecklistRecyclerItem) {
            removeItemFromAdapter(position)

            if (savedDeletedItemForRestoration) {
                saveDeletedItemAndNotifyListener(itemToDelete, position)
            }

            if (adapter.itemCount == 1) {
                val itemInsertionPosition = 0

                addItemToAdapter(
                    ChecklistRecyclerItem(""),
                    itemInsertionPosition
                )

                requestFocusForPositionInAdapter(itemInsertionPosition)
            } else {
                findNextPositionToFocusOnItemDeletion(
                    position,
                    itemToDelete
                )
            }
        }
    }

    private fun findNextPositionToFocusOnItemDeletion(
        position: Int,
        deletedItem: ChecklistRecyclerItem
    ) {
        if (position != newItemPosition && position < adapter.itemCount) {     // Valid suggested position for requesting focus on item
            requestFocusForPositionInAdapter(position)
        } else {
            val isWithinRange: (Int) -> Boolean = {
                it in (position.dec())..(position.inc())
            }
            val validPositionMap = adapter.items
                .filterIndexed { index, item ->
                    (item as? ChecklistRecyclerItem)?.isChecked == deletedItem.isChecked && isWithinRange(index)
                }.map {
                    adapter.items.indexOf(it)
                }

            if (validPositionMap.isEmpty()) {    // No items with the same 'isChecked' flag
                currentPosition = NO_POSITION
                hideKeyboard()
            } else {
                requestFocusForPositionInAdapter(validPositionMap.first())
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

    private fun requestFocusForPositionInAdapter(
        position: Int,
        isStartSelection: Boolean = false,
        isShowKeyboard: Boolean = false
    ) {
        scrollToPosition(position)

        delayHandler.run {
            adapter.requestFocus = ChecklistItemAdapterRequestFocus(
                position = position,
                isStartSelection = isStartSelection,
                isShowKeyboard = isShowKeyboard
            )
        }
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

        onItemDeleted?.invoke(
            item.text,
            item.id
        )
    }
}
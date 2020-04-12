package com.dvdb.checklist.manager

import android.widget.TextView
import com.dvdb.checklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.checklist.recycler.adapter.ChecklistItemAdapterDragListener
import com.dvdb.checklist.recycler.adapter.ChecklistItemAdapterRequestFocus
import com.dvdb.checklist.recycler.holder.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.checklist.recycler.util.DefaultRecyclerItemComparator
import com.dvdb.checklist.recycler.util.RecyclerItemMapper
import com.dvdb.checklist.util.DelayHandler
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

    lateinit var adapter: ChecklistItemAdapter
    lateinit var startDragAndDrop: (position: Int) -> Unit
    lateinit var scrollToPosition: (position: Int) -> Unit

    private val delayHandler: DelayHandler = DelayHandler()

    private var currentPosition: Int = NO_POSITION

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

    override fun onItemChecked(position: Int, isChecked: Boolean) {
        val item = adapter.items[position]

        if (item is ChecklistRecyclerItem) {
            if (isChecked) {
                handleItemChecked(item, position)
            } else {
                handleItemUnchecked(item, position)
            }
        }
    }

    override fun onItemTextChanged(position: Int, text: String) {
        val item = adapter.items[position]

        if (item is ChecklistRecyclerItem) {
            updateItemInAdapter(
                item.copy(text = text),
                position,
                false
            )
        }
    }

    override fun onItemEnterKeyPressed(position: Int, textView: TextView) {
        val currentItem = adapter.items[position]

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
                    newItemText,
                    currentItem.isChecked
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

    override fun onItemFocusChanged(position: Int, hasFocus: Boolean) {
        if (hasFocus) {
            currentPosition = position
        }
    }

    override fun onItemDragHandledClicked(position: Int) {
        startDragAndDrop(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
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

        notifyItemMovedInAdapter(fromPosition, toPosition)
        return true
    }

    override fun canDragOverTargetItem(currentPosition: Int, targetPosition: Int): Boolean {
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
            listOf(
                ChecklistRecyclerItem(
                    ""
                )
            )
        }.plus(ChecklistNewRecyclerItem())
            .sortedWith(DefaultRecyclerItemComparator)

        adapter.items = sortedItems

        if (currentPosition != -1) {
            requestFocusForPositionInAdapter(currentPosition)
        }
    }

    private fun handleItemChecked(item: ChecklistRecyclerItem, position: Int) {
        removeItemFromAdapter(position)

        val positionOfNewItem = adapter.items.indexOfFirst { it is ChecklistNewRecyclerItem }
        val positionOfFirstCheckedItem = adapter.items.indexOfFirst { it is ChecklistRecyclerItem && it.isChecked }
        val toPosition =
            when {
                positionOfFirstCheckedItem != -1 -> positionOfFirstCheckedItem
                positionOfNewItem != -1 -> positionOfNewItem.inc()
                else -> adapter.items.lastIndex
            }.coerceIn(0, adapter.itemCount)

        addItemToAdapter(
            item.copy(isChecked = true),
            toPosition
        )
    }

    private fun handleItemUnchecked(item: ChecklistRecyclerItem, position: Int) {
        removeItemFromAdapter(position)

        val toPosition = adapter.items.indexOfFirst { it is ChecklistNewRecyclerItem }.coerceAtLeast(0)

        addItemToAdapter(
            item.copy(isChecked = false),
            toPosition
        )
    }

    private fun handleDeleteItem(position: Int) {
        val itemToDelete = adapter.items[position]

        if (itemToDelete is ChecklistRecyclerItem) {
            removeItemFromAdapter(position)

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

    private fun findNextPositionToFocusOnItemDeletion(position: Int, deletedItem: ChecklistRecyclerItem) {
        val newItemPosition = adapter.items.indexOfFirst { it is ChecklistNewRecyclerItem }

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
                currentPosition = -1
                hideKeyboard()
            } else {
                requestFocusForPositionInAdapter(validPositionMap.first())
            }
        }
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
        adapter.notifyItemMoved(fromPosition, toPosition)
    }
}
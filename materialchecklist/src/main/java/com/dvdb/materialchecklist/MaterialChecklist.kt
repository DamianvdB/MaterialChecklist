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

package com.dvdb.materialchecklist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.CheckResult
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.config.ChecklistConfig
import com.dvdb.materialchecklist.manager.ChecklistManagerImpl
import com.dvdb.materialchecklist.manager.Manager
import com.dvdb.materialchecklist.manager.TitleManagerImpl
import com.dvdb.materialchecklist.manager.item.ChecklistItem
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.title.TitleRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.materialchecklist.recycler.util.ItemTouchHelperAdapter
import com.dvdb.materialchecklist.recycler.util.RecyclerSpaceItemDecorator
import com.dvdb.materialchecklist.recycler.util.SimpleItemTouchHelper
import com.dvdb.materialchecklist.util.hideKeyboard
import com.dvdb.materialchecklist.util.updateLayoutParams

class MaterialChecklist(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    internal val manager: Manager = Manager(
        TitleManagerImpl(),
        ChecklistManagerImpl(
            hideKeyboard = {
                hideKeyboard()
                requestFocus()
            }
        )
    ) {
        (recyclerView?.adapter as? ChecklistItemAdapter)?.items ?: emptyList()
    }

    internal val config: ChecklistConfig = ChecklistConfig(
        context = context,
        attrs = attrs
    )

    private val recyclerView: RecyclerView

    init {
        addFocusableView()

        recyclerView = createRecyclerView()
        addView(recyclerView)

        val itemTouchCallback =
            SimpleItemTouchHelper(recyclerView.adapter as ItemTouchHelperAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        initManager(recyclerView, itemTouchHelper, itemTouchCallback)

        initDefaultChecklistItems()
    }

    fun setTitleItem(text: String) {
        manager.setTitleItem(text)
    }

    fun removeTitleItem(): Boolean {
        return manager.removeTitleItem()
    }

    fun getTitleItem(): String? {
        return manager.getTitleItem()
    }

    fun setOnTitleItemEnterKeyPressed(onEnterKeyPressed: () -> Unit) {
        manager.onTitleItemEnterKeyPressed = onEnterKeyPressed
    }

    fun setOnTitleItemActionIconClicked(onActionIconClicked: () -> Unit) {
        manager.onTitleItemActionIconClicked = onActionIconClicked
    }

    /**
     * Set the list of checklist items by parsing the [formattedText] string.
     *
     * @param formattedText The formatted string to parse containing the checklist items.
     */
    fun setItems(formattedText: String) {
        manager.setItems(formattedText)
    }

    /**
     * Get the formatted string representation of the checklist items.
     *
     * Be careful with editing the result of this method. Edits to the returned string may result in
     * the loss of state to the checklist items.
     *
     * @param keepCheckboxSymbols The flag to keep or remove the checkbox symbols of the checklist items.
     * @param keepCheckedItems The flag to keep or remove the checklist items that are marked as checked.
     * @return The formatted string representation of the checklist items.
     */
    @CheckResult
    fun getItems(
        keepCheckboxSymbols: Boolean = true,
        keepCheckedItems: Boolean = true
    ): String {
        return manager.getFormattedTextItems(
            keepCheckboxSymbols,
            keepCheckedItems
        )
    }

    /**
     * Set a listener for when a checklist item is deleted.
     *
     * @param listener The listener to be notified when a checklist item is deleted.
     */
    fun setOnItemDeletedListener(listener: ((text: String, itemId: Long) -> Unit)) {
        manager.onItemDeleted = listener
    }

    /**
     * Restore deleted checklist items.
     *
     * @param itemIds The id's of the checklist items to restore.
     * @return 'true' if all items were restored, otherwise 'false'.
     */
    fun restoreDeleteItems(itemIds: List<Long>): Boolean {
        return manager.restoreDeleteItems(itemIds)
    }

    /**
     * Restore a deleted checklist item.
     *
     * @param itemId The id of the checklist item to restore.
     * @return 'true' if the item was restored, otherwise 'false'.
     */
    fun restoreDeletedItem(itemId: Long): Boolean {
        return manager.restoreDeletedItem(itemId)
    }

    /**
     * Remove all the checklist items that are marked as checked.
     * These items can be restored using their id's.
     *
     * @return id's of the checklist items removed.
     */
    fun removeAllCheckedItems(): List<Long> {
        return manager.removeAllCheckedItems()
    }

    /**
     * Uncheck all the checklist items that are marked as checked.
     *
     * @return 'true' if any checked items were marked as unchecked, otherwise 'false'.
     */
    fun uncheckAllCheckedItems(): Boolean {
        return manager.uncheckAllCheckedItems()
    }

    /**
     * Get the total number of checklist items.
     *
     * @return number of checklist items.
     */
    @CheckResult
    fun getItemCount(): Int {
        return manager.getItemCount()
    }

    /**
     * Get the total number of checklist items that are marked as checked.
     *
     * @return number of checklist items marked as checked.
     */
    @CheckResult
    fun getCheckedItemCount(): Int {
        return manager.getCheckedItemCount()
    }

    /**
     * Get the position of the checklist item in the list that has focus.
     *
     * @return checklist item focus position, otherwise -1 if no item has focus.
     */
    @CheckResult
    fun getItemFocusPosition(): Int {
        return manager.getItemFocusPosition()
    }

    /**
     * Set the focus on the checklist item at [position] in the list,
     * with the selection at the end of the item's text.
     *
     * @return 'true' if focus could be set on a checklist item, otherwise 'false.
     */
    fun setItemFocusPosition(position: Int): Boolean {
        return manager.setItemFocusPosition(position)
    }

    /**
     * Get the checklist item at [position] in the list.
     *
     * @return checklist item at [position] or null if no item could be found
     * at [position] in the list.
     */
    @CheckResult
    fun getChecklistItemAtPosition(position: Int): ChecklistItem? {
        return manager.getChecklistItemAtPosition(position)
    }

    /**
     * Update the checklist [item] in the list with same id.
     *
     * @return 'true' if a checklist item with the same id could be found
     * and it has different values when compared to [item]. Otherwise, return 'false'
     * if a checklist item could not be found or they have same values.
     */
    fun updateChecklistItem(item: ChecklistItem): Boolean {
        return manager.updateChecklistItem(item)
    }

    private fun addFocusableView() {
        val focusableView = View(context)
        focusableView.isFocusableInTouchMode = true

        addView(focusableView)

        focusableView.updateLayoutParams {
            height = 1
        }
    }

    private fun createRecyclerView(): RecyclerView {
        val recyclerView = RecyclerView(context)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        val enterActionPerformedFactory = EnterActionPerformedFactory()
        recyclerView.adapter = ChecklistItemAdapter(
            config = config.toAdapterConfig(),
            itemTitleRecyclerHolderFactory = TitleRecyclerHolder.Factory(
                enterActionPerformedFactory,
                manager
            ),
            itemRecyclerHolderFactory = ChecklistRecyclerHolder.Factory(
                enterActionPerformedFactory,
                manager
            ),
            itemNewRecyclerHolderFactory = ChecklistNewRecyclerHolder.Factory(
                manager.onCreateNewChecklistItemClicked
            ),
            itemDragListener = manager
        )

        return recyclerView
    }

    private fun initManager(
        recyclerView: RecyclerView,
        itemTouchHelper: ItemTouchHelper,
        itemTouchCallback: SimpleItemTouchHelper
    ) {
        val adapter = recyclerView.adapter as ChecklistItemAdapter

        manager.lateInitTitleState(
            adapter = adapter,
            config = config.totTitleManagerConfig()
        )

        manager.lateInitState(
            adapter = adapter,
            config = config.toManagerConfig(),
            scrollToPosition = createManagerScrollToPositionFunction(recyclerView),
            startDragAndDrop = createManagerStartDragAndDropFunction(recyclerView, itemTouchHelper),
            enableDragAndDrop = createManagerEnableDragAndDropFunction(itemTouchCallback),
            updateItemPadding = createManagerUpdateItemPaddingFunction(recyclerView),
            enableItemAnimations = createManagerEnableItemAnimationsFunction(recyclerView)
        )
    }

    private fun initDefaultChecklistItems() {
        if (isInEditMode) {
            setItems(
                "[ ] Send meeting notes to team\n" +
                        "[ ] Order flowers\n" +
                        "[ ] Organise camera gear\n" +
                        "[ ] Book flights to Dubai\n" +
                        "[x] Lease out holiday home"
            )
        } else {
            setItems("")
        }
    }

    /**
     * Creates a function for the checklist manager for scrolling to a checklist item at the provided position
     * in the recycler view.
     *
     * @param recyclerView The recycler view to use for scrolling to the checklist.
     */
    private fun createManagerScrollToPositionFunction(recyclerView: RecyclerView): (position: Int) -> Unit {
        return { position ->
            if (position != RecyclerView.NO_POSITION) {
                recyclerView.layoutManager?.scrollToPosition(position)
            }
        }
    }

    /**
     * Creates a function for the checklist manager for starting the drag-and-drop functionality
     * for a checklist item at the provided position in the recycler view.
     *
     * @param recyclerView The recycler view containing the checklist item to use as the target.
     * @param itemTouchHelper The item touch helper to use for starting the drag-and-drop functionality.
     */
    private fun createManagerStartDragAndDropFunction(
        recyclerView: RecyclerView,
        itemTouchHelper: ItemTouchHelper
    ): (position: Int) -> Unit {
        return { position ->
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
            if (viewHolder != null) {
                itemTouchHelper.startDrag(viewHolder)
            }
        }
    }

    /**
     * Creates a function for the checklist manager for enabling/disabling the drag-and-drop functionality
     * for checklist items.
     *
     * @param itemTouchCallback The item touch callback helper to use for enabling/disabling the drag-and-drop functionality.
     */
    private fun createManagerEnableDragAndDropFunction(itemTouchCallback: SimpleItemTouchHelper): (isEnabled: Boolean) -> Unit {
        return { isEnabled ->
            itemTouchCallback.setIsDragEnabled(isEnabled)
        }
    }

    /**
     * Creates a function for the checklist manager for updating the padding of the first and last checklist items
     * in the recycler view.
     *
     * @param recyclerView The recycler view containing the checklist items.
     */
    private fun createManagerUpdateItemPaddingFunction(recyclerView: RecyclerView): (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit {
        val defaultItemAnimator: RecyclerView.ItemAnimator? = recyclerView.itemAnimator

        return { firstItemTopPadding, lastItemBottomPadding ->
            for (index in 0 until recyclerView.itemDecorationCount) {
                val itemDecoration = recyclerView.getItemDecorationAt(index)
                if (itemDecoration is RecyclerSpaceItemDecorator) {
                    recyclerView.removeItemDecoration(itemDecoration)
                    break
                }
            }

            if (firstItemTopPadding != null || lastItemBottomPadding != null) {
                recyclerView.addItemDecoration(
                    if (defaultItemAnimator != null) {
                        RecyclerSpaceItemDecorator(
                            firstItemMargin = firstItemTopPadding?.toInt() ?: 0,
                            lastItemMargin = lastItemBottomPadding?.toInt() ?: 0,
                            defaultItemAnimator = defaultItemAnimator
                        )
                    } else {
                        RecyclerSpaceItemDecorator(
                            firstItemMargin = firstItemTopPadding?.toInt() ?: 0,
                            lastItemMargin = lastItemBottomPadding?.toInt() ?: 0
                        )
                    }
                )
            }
        }
    }

    /**
     * Creates a function for the checklist manager for enabling/disabling the item animations (add, remove, change, etc.)
     * of the recycler view.
     *
     * @param recyclerView The recycler view to use for enabling/disabling the item animations.
     */
    private fun createManagerEnableItemAnimationsFunction(recyclerView: RecyclerView): (isEnabled: Boolean) -> Unit {
        var itemAnimator: RecyclerView.ItemAnimator? = null

        return { isEnabled ->
            if (recyclerView.itemAnimator != null && recyclerView.itemAnimator != itemAnimator) {
                itemAnimator = recyclerView.itemAnimator
            }

            if (isEnabled) {
                recyclerView.itemAnimator = itemAnimator
            } else {
                recyclerView.itemAnimator = null
            }
        }
    }
}

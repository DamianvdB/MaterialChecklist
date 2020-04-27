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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.config.ChecklistConfig
import com.dvdb.materialchecklist.manager.ChecklistManager
import com.dvdb.materialchecklist.manager.setItems
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.materialchecklist.recycler.util.ItemTouchHelperAdapter
import com.dvdb.materialchecklist.recycler.util.RecyclerSpaceItemDecorator
import com.dvdb.materialchecklist.recycler.util.SimpleItemTouchHelper
import com.dvdb.materialchecklist.util.hideKeyboard

class MaterialChecklist(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    internal val manager: ChecklistManager = ChecklistManager(
        hideKeyboard = {
            hideKeyboard()
            requestFocus()
        }
    )

    internal val config: ChecklistConfig = ChecklistConfig(
        context = context,
        attrs = attrs
    )

    init {
        initLayout()

        val recyclerView = createRecyclerView()
        addView(recyclerView)

        val itemTouchCallback = SimpleItemTouchHelper(recyclerView.adapter as ItemTouchHelperAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        initManager(recyclerView, itemTouchHelper, itemTouchCallback)

        if (isInEditMode) {
            setItems(
                "[ ] Send meeting notes to team\n" +
                        "[ ] Order flowers\n" +
                        "[ ] Organise camera gear \n" +
                        "[ ] Book flights to Dubai\n" +
                        "[x] Lease out holiday home"
            )
        }
    }

    private fun initLayout() {
        isFocusableInTouchMode = true
        requestFocus()
    }

    private fun createRecyclerView(): RecyclerView {
        val recyclerView = RecyclerView(context)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.adapter = ChecklistItemAdapter(
            config = config.toAdapterConfig(),
            itemRecyclerHolderFactory = ChecklistRecyclerHolder.Factory(
                EnterActionPerformedFactory(),
                manager
            ),
            itemNewRecyclerHolderFactory = ChecklistNewRecyclerHolder.Factory(
                manager.onNewChecklistListItemClicked
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
        manager.lateInitState(
            adapter = recyclerView.adapter as ChecklistItemAdapter,
            config = config.toManagerConfig(),
            scrollToPosition = createManagerScrollToPositionFunction(recyclerView),
            startDragAndDrop = createManagerStartDragAndDropFunction(recyclerView, itemTouchHelper),
            enableDragAndDrop = createManagerEnableDragAndDropFunction(itemTouchCallback),
            updateItemPadding = createManagerUpdateItemPaddingFunction(recyclerView),
            enableItemAnimations = createManagerEnableItemAnimationsFunction(recyclerView)
        )
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
                    RecyclerSpaceItemDecorator(
                        firstItemMargin = firstItemTopPadding?.toInt() ?: 0,
                        lastItemMargin = lastItemBottomPadding?.toInt() ?: 0
                    )
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

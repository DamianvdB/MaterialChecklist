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
            scrollToPosition = { position ->
                if (position != RecyclerView.NO_POSITION) {
                    recyclerView.layoutManager?.scrollToPosition(position)
                }
            },
            startDragAndDrop = { position ->
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            },
            enableDragAndDrop = { isEnabled ->
                itemTouchCallback.setIsDragEnabled(isEnabled)
            }
        )
    }
}

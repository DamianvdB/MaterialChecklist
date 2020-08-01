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

package com.dvdb.materialchecklist.manager.checklist

import com.dvdb.materialchecklist.manager.checklist.model.ChecklistItem
import com.dvdb.materialchecklist.manager.checklist.model.ChecklistManagerConfig
import com.dvdb.materialchecklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.materialchecklist.recycler.adapter.listener.ChecklistItemAdapterDragListener
import com.dvdb.materialchecklist.recycler.checklist.listener.ChecklistRecyclerHolderItemListener

/**
 * Manages the state and behavior of the checklist items.
 */
internal interface ChecklistManager :
    ChecklistRecyclerHolderItemListener,
    ChecklistItemAdapterDragListener,
    ChecklistFocusRequester {

    val onCreateNewChecklistItemClicked: (position: Int) -> Unit

    var onItemDeleted: ((text: String, id: Long) -> Unit)

    fun lateInitState(
        adapter: ChecklistItemAdapter,
        config: ChecklistManagerConfig,
        scrollToPosition: (position: Int) -> Unit,
        startDragAndDrop: (position: Int) -> Unit,
        enableDragAndDrop: (isEnabled: Boolean) -> Unit,
        updateItemPadding: (firstItemTopPadding: Float?, lastItemBottomPadding: Float?) -> Unit,
        enableItemAnimations: (isEnabled: Boolean) -> Unit
    )

    fun setItems(formattedText: String)

    fun getFormattedTextItems(
        keepCheckboxSymbols: Boolean,
        keepCheckedItems: Boolean
    ): String

    fun setConfig(config: ChecklistManagerConfig)

    fun restoreDeletedItems(itemIds: List<Long>): Boolean

    fun restoreDeletedItem(itemId: Long): Boolean

    fun removeAllCheckedItems(): List<Long>

    fun uncheckAllCheckedItems(): Boolean

    fun getCheckedItemCount(): Int

    fun getChecklistItemAtPosition(position: Int): ChecklistItem?

    fun updateChecklistItem(item: ChecklistItem): Boolean
}
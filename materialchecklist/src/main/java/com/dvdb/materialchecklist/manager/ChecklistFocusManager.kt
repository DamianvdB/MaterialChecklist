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

import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem

/**
 * Manages the focus and the selection behavior of the checklist items.
 */
internal interface ChecklistFocusManager : ChecklistFocusRequester {

    fun onItemFocusChanged(
        position: Int,
        startSelection: Int,
        hasFocus: Boolean
    )

    fun onItemSelectionChanged(
        position: Int,
        startSelection: Int,
        hasFocus: Boolean
    )

    fun onNewItemCreated(position: Int)

    fun onItemPreCheckedStateChanged(position: Int)

    fun onItemChecked(
        originalPosition: Int,
        updatedPosition: Int,
        item: ChecklistRecyclerItem
    )

    fun onItemUnchecked(
        originalPosition: Int,
        updatedPosition: Int,
        item: ChecklistRecyclerItem
    )

    fun onItemDeleted(
        position: Int,
        item: ChecklistRecyclerItem,
        isDeletedIconClicked: Boolean
    )

    fun onItemUpdated(
        position: Int,
        item: ChecklistRecyclerItem
    )

    fun onAllCheckedItemsRemoved(itemPositions: List<Int>)

    fun onItemDragStarted(position: Int)

    fun onItemDragPositionChanged(
        fromPosition: Int,
        toPosition: Int
    )
}
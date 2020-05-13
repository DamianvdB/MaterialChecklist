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

import androidx.annotation.CheckResult
import com.dvdb.materialchecklist.MaterialChecklist
import com.dvdb.materialchecklist.manager.item.ChecklistItem

/**
 * Set the list of checklist items by parsing the [formattedText] string.
 *
 * @param formattedText The formatted string to parse containing the checklist items.
 */
fun MaterialChecklist.setItems(formattedText: String) {
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
fun MaterialChecklist.getItems(
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
fun MaterialChecklist.setOnItemDeletedListener(listener: ((text: String, itemId: Long) -> Unit)) {
    manager.onItemDeleted = listener
}

/**
 * Restore deleted checklist items.
 *
 * @param itemIds The id's of the checklist items to restore.
 * @return 'true' if all items were restored, otherwise 'false'.
 */
fun MaterialChecklist.restoreDeleteItems(itemIds: List<Long>): Boolean {
    return manager.restoreDeleteItems(itemIds)
}

/**
 * Restore a deleted checklist item.
 *
 * @param itemId The id of the checklist item to restore.
 * @return 'true' if the item was restored, otherwise 'false'.
 */
fun MaterialChecklist.restoreDeletedItem(itemId: Long): Boolean {
    return manager.restoreDeletedItem(itemId)
}

/**
 * Remove all the checklist items that are marked as checked.
 * These items can be restored using their id's.
 *
 * @return id's of the checklist items removed.
 */
fun MaterialChecklist.removeAllCheckedItems(): List<Long> {
    return manager.removeAllCheckedItems()
}

/**
 * Uncheck all the checklist items that are marked as checked.
 *
 * @return 'true' if any checked items were marked as unchecked, otherwise 'false'.
 */
fun MaterialChecklist.uncheckAllCheckedItems(): Boolean {
    return manager.uncheckAllCheckedItems()
}

/**
 * Get the total number of checklist items.
 *
 * @return number of checklist items.
 */
@CheckResult
fun MaterialChecklist.getItemCount(): Int {
    return manager.getItemCount()
}

/**
 * Get the total number of checklist items that are marked as checked.
 *
 * @return number of checklist items marked as checked.
 */
@CheckResult
fun MaterialChecklist.getCheckedItemCount(): Int {
    return manager.getCheckedItemCount()
}

/**
 * Get the position of the checklist item in the list that has focus.
 *
 * @return checklist item focus position, otherwise -1 if no item has focus.
 */
@CheckResult
fun MaterialChecklist.getItemFocusPosition(): Int {
    return manager.getItemFocusPosition()
}

/**
 * Set the focus on the checklist item at [position] in the list,
 * with the selection at the end of the item's text.
 *
 * @return 'true' if focus could be set on a checklist item, otherwise 'false.
 */
fun MaterialChecklist.setItemFocusPosition(position: Int): Boolean {
    return manager.setItemFocusPosition(position)
}

/**
 * Get the checklist item at [position] in the list.
 *
 * @return checklist item at [position] or null if no item could be found
 * at [position] in the list.
 */
@CheckResult
fun MaterialChecklist.getChecklistItemAtPosition(position: Int): ChecklistItem? {
    return manager.getChecklistItemAtPosition(position)
}

/**
 * Update the checklist [item] in the list with same id.
 *
 * @return 'true' if a checklist item with the same id could be found
 * and it has different values when compared to [item]. Otherwise, return 'false'
 * if a checklist item could not be found or they have same values.
 */
fun MaterialChecklist.updateChecklistItem(item: ChecklistItem): Boolean {
    return manager.updateChecklistItem(item)
}
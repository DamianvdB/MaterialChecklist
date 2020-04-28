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

import com.dvdb.materialchecklist.MaterialChecklist

/**
 * Sets the list of checklist items by parsing the [formattedText] string.
 *
 * @param formattedText The formatted string to parse containing the checklist items.
 */
fun MaterialChecklist.setItems(formattedText: String) {
    manager.setItems(formattedText)
}

/**
 * Gets the formatted string representation of the checklist items.
 *
 * Be careful with editing the result of this method. Edits to the returned string may result in
 * the loss of state to the checklist items.
 *
 * @param keepCheckboxSymbols The flag to keep or remove the checkbox symbols of the checklist items.
 * @param keepCheckedItems The flag to keep or remove the checklist items that are marked as checked.
 * @return The formatted string representation of the checklist items.
 */
fun MaterialChecklist.getFormattedTextItems(
    keepCheckboxSymbols: Boolean = true,
    keepCheckedItems: Boolean = true
): String {
    return manager.getFormattedTextItems(
        keepCheckboxSymbols,
        keepCheckedItems
    )
}

/**
 * Sets a listener for when a checklist item is deleted.
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
 * Remove all the checklist items that are checked.
 * These items can be restored using their id's.
 *
 * @return id's of the checklist items removed.
 */
fun MaterialChecklist.removeAllCheckedItems(): List<Long> {
    return manager.removeAllCheckedItems()
}

/**
 * Uncheck all the checklist items that are checked.
 *
 * @return 'true' if any checked items were marked as unchecked, otherwise 'false'.
 */
fun MaterialChecklist.uncheckAllCheckedItems(): Boolean {
    return manager.uncheckAllCheckedItems()
}
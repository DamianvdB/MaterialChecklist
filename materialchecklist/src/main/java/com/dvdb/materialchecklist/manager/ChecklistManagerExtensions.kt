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

fun MaterialChecklist.setItems(formattedText: String) {
    manager.setItems(formattedText)
}

fun MaterialChecklist.getFormattedTextItems(
    keepCheckedItems: Boolean = true,
    skipCheckedItems: Boolean = false
): String {
    return manager.getFormattedTextItems(
        keepCheckedItems,
        skipCheckedItems
    )
}

fun MaterialChecklist.setOnItemDeletedListener(listener: ((text: String, itemId: String) -> Unit)) {
    manager.onItemDeleted = listener
}

/**
 * Restore deleted checklist items.
 *
 * @param itemIds The id's of the checklist items to restore.
 * @return 'true' if all items were restored, otherwise 'false'.
 */
fun MaterialChecklist.restoreDeleteItems(itemIds: List<String>): Boolean {
    return manager.restoreDeleteItems(itemIds)
}

/**
 * Restore a deleted checklist item.
 *
 * @param itemId The id of the checklist item to restore.
 * @return 'true' if item was restored, otherwise 'false'.
 */
fun MaterialChecklist.restoreDeletedItem(itemId: String): Boolean {
    return manager.restoreDeletedItem(itemId)
}

/**
 * Remove all checklist items that are checked.
 * These items can be restored using their id's.
 *
 * @return id's of checklist items removed.
 */
fun MaterialChecklist.removeAllCheckedItems(): List<String> {
    return manager.removeAllCheckedItems()
}
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

fun MaterialChecklist.restoreDeletedItem(itemId: String): Boolean {
    return manager.restoreDeletedItem(itemId)
}
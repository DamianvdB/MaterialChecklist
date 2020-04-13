package com.dvdb.checklist.manager

import com.dvdb.checklist.Checklist

fun Checklist.setItems(formattedText: String) {
    manager.setItems(formattedText)
}

fun Checklist.getFormattedTextItems(
    keepCheckedItems: Boolean = true,
    skipCheckedItems: Boolean = false
): String {
    return manager.getFormattedTextItems(
        keepCheckedItems,
        skipCheckedItems
    )
}

fun Checklist.setOnItemDeletedListener(listener: ((text: String, itemId: String) -> Unit)) {
    manager.onItemDeleted = listener
}

fun Checklist.restoreDeletedItem(itemId: String): Boolean {
    return manager.restoreDeletedItem(itemId)
}
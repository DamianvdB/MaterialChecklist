package com.dvdb.checklist.util

import android.annotation.SuppressLint
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem

private const val CHECKED_SYMBOL = "[x]"
private const val UNCHECKED_SYMBOL = "[ ]"
private const val DELIMINATOR_CHARACTER = "\n"

internal object RecyclerItemMapper {

    fun toFormattedText(items: List<ChecklistRecyclerItem>): String {
        return items.joinToString(separator = "") { item ->
            val prefix = if (item.isChecked) CHECKED_SYMBOL else UNCHECKED_SYMBOL
            val suffix = if (item == items.last()) "" else DELIMINATOR_CHARACTER
            "$prefix ${item.text}$suffix"
        }
    }

    @SuppressLint("DefaultLocale")
    fun toItems(formattedText: String): List<BaseRecyclerItem> {
        return formattedText.split(DELIMINATOR_CHARACTER).map { text ->
            if (text.isFormatValid()) {
                ChecklistRecyclerItem(
                    text.substring(4, text.length),
                    text.substring(0, 3).toLowerCase().isChecked()
                )
            } else {
                ChecklistRecyclerItem(text)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun String.isFormatValid(): Boolean {
        return substring(0, 4).toLowerCase().run { this == "$UNCHECKED_SYMBOL " || this == "$CHECKED_SYMBOL " }
    }

    private fun String.isChecked(): Boolean {
        return when (this) {
            CHECKED_SYMBOL -> true
            else -> false
        }
    }
}
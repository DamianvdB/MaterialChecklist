package com.dvdb.checklist.recycler.util

import android.annotation.SuppressLint
import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.util.substringOrNull

private const val CHECKED_SYMBOL = "[x]"
private const val UNCHECKED_SYMBOL = "[ ]"
private const val DELIMINATOR_CHARACTER = "\n"
private const val ITEM_SEPARATOR_CHARACTER = ""

internal object RecyclerItemMapper {

    fun toFormattedText(
        items: List<BaseRecyclerItem>,
        keepCheckSymbols: Boolean,
        skipCheckedItems: Boolean
    ): String {
        return items.filterIsInstance<ChecklistRecyclerItem>()
            .joinToString(separator = ITEM_SEPARATOR_CHARACTER) { item ->
                if (!skipCheckedItems || !item.isChecked) {
                    val prefix = if (keepCheckSymbols) {
                        (if (item.isChecked) CHECKED_SYMBOL else UNCHECKED_SYMBOL) + " "
                    } else {
                        ""
                    }
                    val suffix = if (item == items.last()) "" else DELIMINATOR_CHARACTER

                    "$prefix${item.text}$suffix"
                } else {
                    ""
                }
            }
    }

    @SuppressLint("DefaultLocale")
    fun toItems(formattedText: String): List<BaseRecyclerItem> {
        return formattedText.split(DELIMINATOR_CHARACTER).mapNotNull { text ->
            when {
                text.isFormatValid() -> {
                    ChecklistRecyclerItem(
                        text.substring(4, text.length),
                        text.substring(0, 3).toLowerCase().isChecked()
                    )
                }
                text.isNotEmpty() -> ChecklistRecyclerItem(text)
                else -> null
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun String.isFormatValid(): Boolean {
        return substringOrNull(0, 4)
            ?.toLowerCase()
            .run { this == "$UNCHECKED_SYMBOL " || this == "$CHECKED_SYMBOL " }
    }

    private fun String.isChecked(): Boolean {
        return when (this) {
            CHECKED_SYMBOL -> true
            else -> false
        }
    }
}
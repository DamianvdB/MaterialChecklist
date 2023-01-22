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

package com.dvdb.materialchecklist.recycler.util

import android.annotation.SuppressLint
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.util.substringOrNull

private const val CHECKED_SYMBOL = "[x]"
private const val UNCHECKED_SYMBOL = "[ ]"
private const val DELIMINATOR_CHARACTER = "\n"
private const val ITEM_SEPARATOR_CHARACTER = ""

internal object RecyclerItemMapper {

    fun toFormattedText(
        items: List<BaseRecyclerItem>,
        keepCheckboxSymbols: Boolean,
        keepCheckedItems: Boolean
    ): String {
        return items.filterIsInstance<ChecklistRecyclerItem>()
            .joinToString(separator = ITEM_SEPARATOR_CHARACTER) { item ->
                if ((keepCheckedItems || !item.isChecked) &&
                    (item.text.isNotEmpty() || items.size > 1)
                ) {
                    val prefix = if (keepCheckboxSymbols) {
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
                        text.substring(0, 3).lowercase().isChecked()
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
            ?.lowercase()
            .run { this == "$UNCHECKED_SYMBOL " || this == "$CHECKED_SYMBOL " }
    }

    private fun String.isChecked(): Boolean {
        return when (this) {
            CHECKED_SYMBOL -> true
            else -> false
        }
    }
}
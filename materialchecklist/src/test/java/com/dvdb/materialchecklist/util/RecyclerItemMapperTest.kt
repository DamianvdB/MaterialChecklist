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

package com.dvdb.materialchecklist.util

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.util.RecyclerItemMapper
import org.junit.Assert
import org.junit.Test

internal class RecyclerItemMapperTest {

    private val validChecklistItems = listOf(
        ChecklistRecyclerItem("Buy beer", true),
        ChecklistRecyclerItem("Buy steak", true),
        ChecklistRecyclerItem("Buy brandy", true),
        ChecklistRecyclerItem("Buy Coca-Cola", false),
        ChecklistRecyclerItem("Buy Portuguese buns", true)
    ).resetIds()

    private val validChecklistContents = "[x] Buy beer\n[x] Buy steak\n[x] Buy brandy\n[ ] Buy Coca-Cola\n[x] Buy Portuguese buns"

    @Test
    fun toFormattedText_empty() {
        val expectedContents = ""
        val inputItems = emptyList<BaseRecyclerItem>()
        val actualContents = RecyclerItemMapper.toFormattedText(
            inputItems,
            keepCheckboxSymbols = true,
            keepCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckboxSymbolsTrue_andKeepCheckedItemsTrue() {
        val expectedContents = validChecklistContents
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckboxSymbols = true,
            keepCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckboxSymbolsFalse_andKeepCheckedItemsTrue() {
        val expectedContents = validChecklistContents.replace("[x] ", "").replace("[ ] ", "")
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckboxSymbols = false,
            keepCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckboxSymbolsFalse_andKeepCheckedItemsFalse() {
        val expectedContents = "Buy Coca-Cola\n"
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckboxSymbols = false,
            keepCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckboxSymbolsTrue_andKeepCheckedItemsFalse() {
        val expectedContents = "[ ] Buy Coca-Cola\n"
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckboxSymbols = true,
            keepCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withMalformedCheckboxSymbols() {
        val expectedContents = "[x] [x] Buy beer\n[x] [ ] Buy steak\n[x] [X]Buy brandy\n[ ] [ ] Buy Coca-Cola\n[x] [] Buy Portuguese buns"
        val inputItems = listOf(
            ChecklistRecyclerItem("[x] Buy beer", true),
            ChecklistRecyclerItem("[ ] Buy steak", true),
            ChecklistRecyclerItem("[X]Buy brandy", true),
            ChecklistRecyclerItem("[ ] Buy Coca-Cola", false),
            ChecklistRecyclerItem("[] Buy Portuguese buns", true)
        )
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckboxSymbols = true,
            keepCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toItems() {
        val expectedItems = validChecklistItems
        val inputText = validChecklistContents
        val actualItems = RecyclerItemMapper.toItems(inputText).resetIds()

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun toItems_empty() {
        val expectedItems = emptyList<BaseRecyclerItem>()
        val inputText = ""
        val actualItems = RecyclerItemMapper.toItems(inputText)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun toItems_withMalformedCheckboxSymbol() {
        val expectedItems = listOf(
            ChecklistRecyclerItem("    Missing check symbol"),
            ChecklistRecyclerItem("[x]Missing space after symbol"),
            ChecklistRecyclerItem("[   Malformed check symbol 1"),
            ChecklistRecyclerItem(" x  Malformed check symbol 2"),
            ChecklistRecyclerItem(" ]  Malformed check symbol 3"),
            ChecklistRecyclerItem("Malformed check symbol 4")
        ).resetIds()
        val inputContent =
            "    Missing check symbol\n[x]Missing space after symbol\n[   Malformed check symbol 1\n x  Malformed check symbol 2\n ]  Malformed check symbol 3\nMalformed check symbol 4"
        val actualItems = RecyclerItemMapper.toItems(inputContent).resetIds()

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun toItems_withMalformedCheckboxSymbolsInUpperCase() {
        val expectedItems = validChecklistItems
        val inputText = validChecklistContents.replace("[x]", "[X]")
        val actualItems = RecyclerItemMapper.toItems(inputText).resetIds()

        Assert.assertEquals(expectedItems, actualItems)
    }
}
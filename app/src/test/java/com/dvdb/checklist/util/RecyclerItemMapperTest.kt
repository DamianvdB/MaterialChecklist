package com.dvdb.checklist.util

import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.recycler.util.RecyclerItemMapper
import org.junit.Assert
import org.junit.Test

internal class RecyclerItemMapperTest {

    private val validChecklistItems = listOf(
        ChecklistRecyclerItem("Buy beer", true),
        ChecklistRecyclerItem("Buy steak", true),
        ChecklistRecyclerItem("Buy brandy", true),
        ChecklistRecyclerItem("Buy Coca-Cola", false),
        ChecklistRecyclerItem("Buy Portuguese buns", true)
    )

    private val validChecklistContents = "[x] Buy beer\n[x] Buy steak\n[x] Buy brandy\n[ ] Buy Coca-Cola\n[x] Buy Portuguese buns"

    @Test
    fun toFormattedText_empty() {
        val expectedContents = ""
        val inputItems = emptyList<BaseRecyclerItem>()
        val actualContents = RecyclerItemMapper.toFormattedText(
            inputItems,
            keepCheckSymbols = true,
            skipCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckedSymbolsTrue_andSkipCheckedItemsFalse() {
        val expectedContents = validChecklistContents
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckSymbols = true,
            skipCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckSymbolsFalse_andSkipCheckedItemsFalse() {
        val expectedContents = validChecklistContents.replace("[x] ", "").replace("[ ] ", "")
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckSymbols = false,
            skipCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withKeepCheckSymbolsFalse_andSkipCheckedItemsTrue() {
        val expectedContents = "Buy Coca-Cola\n"
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckSymbols = false,
            skipCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withSkipCheckedItemsTrue_andSkipCheckedItemsTrue() {
        val expectedContents = "[ ] Buy Coca-Cola\n"
        val inputItems = validChecklistItems
        val actualContents = RecyclerItemMapper.toFormattedText(
            items = inputItems,
            keepCheckSymbols = true,
            skipCheckedItems = true
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toFormattedText_withMalformedCheckSymbols() {
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
            keepCheckSymbols = true,
            skipCheckedItems = false
        )

        Assert.assertEquals(expectedContents, actualContents)
    }

    @Test
    fun toItems() {
        val expectedItems = validChecklistItems
        val inputText = validChecklistContents
        val actualItems = RecyclerItemMapper.toItems(inputText)

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
    fun toItems_withMalformedCheckSymbol() {
        val expectedItems = listOf(
            ChecklistRecyclerItem("    Missing check symbol"),
            ChecklistRecyclerItem("[x]Missing space after symbol"),
            ChecklistRecyclerItem("[   Malformed check symbol 1"),
            ChecklistRecyclerItem(" x  Malformed check symbol 2"),
            ChecklistRecyclerItem(" ]  Malformed check symbol 3"),
            ChecklistRecyclerItem("Malformed check symbol 4")
        )
        val inputContent =
            "    Missing check symbol\n[x]Missing space after symbol\n[   Malformed check symbol 1\n x  Malformed check symbol 2\n ]  Malformed check symbol 3\nMalformed check symbol 4"
        val actualItems = RecyclerItemMapper.toItems(inputContent)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun toItems_withMalformedCheckSymbolsInUpperCase() {
        val expectedItems = validChecklistItems
        val inputText = validChecklistContents.replace("[x]", "[X]")
        val actualItems = RecyclerItemMapper.toItems(inputText)

        Assert.assertEquals(expectedItems, actualItems)
    }
}
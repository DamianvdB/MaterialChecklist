package com.dvdb.checklist.util

import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.checklist.recycler.util.DefaultRecyclerItemComparator
import org.junit.Assert
import org.junit.Test

class DefaultRecyclerItemComparatorTest {

    @Test
    fun onlyUncheckedItemsTest() {
        val inputItems = listOf<BaseRecyclerItem>(
            ChecklistRecyclerItem("Item 1"),
            ChecklistRecyclerItem("Item 2"),
            ChecklistRecyclerItem("Item 3"),
            ChecklistRecyclerItem("Item 4"),
            ChecklistRecyclerItem("Item 5"),
            ChecklistRecyclerItem("Item 6")
        )
        val expectedItems = inputItems
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun uncheckedItemsAndNewItemTest() {
        val inputItems = listOf(
            ChecklistRecyclerItem("Item 1"),
            ChecklistRecyclerItem("Item 2"),
            ChecklistRecyclerItem("Item 3"),
            ChecklistNewRecyclerItem(),
            ChecklistRecyclerItem("Item 4"),
            ChecklistRecyclerItem("Item 5"),
            ChecklistRecyclerItem("Item 6")
        )
        val expectedItems = listOf(
            ChecklistRecyclerItem("Item 1"),
            ChecklistRecyclerItem("Item 2"),
            ChecklistRecyclerItem("Item 3"),
            ChecklistRecyclerItem("Item 4"),
            ChecklistRecyclerItem("Item 5"),
            ChecklistRecyclerItem("Item 6"),
            ChecklistNewRecyclerItem()
        )
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun onlyCheckedItemsTest() {
        val inputItems = listOf<BaseRecyclerItem>(
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", true),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", true)
        )
        val expectedItems = inputItems
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun checkedItemsAndNewItemTest() {
        val inputItems = listOf(
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", true),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", true),
            ChecklistNewRecyclerItem()
        )
        val expectedItems = listOf(
            ChecklistNewRecyclerItem(),
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", true),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", true)
        )
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun checkedAndUncheckedItemsTest() {
        val inputItems = listOf<BaseRecyclerItem>(
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", false)
        )
        val expectedItems = listOf<BaseRecyclerItem>(
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 6", false),
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 5", true)
        )
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun checkedItemsAndUncheckedItemsAndNewItemTest() {
        val inputItems = listOf(
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", false),
            ChecklistNewRecyclerItem()
        )
        val expectedItems = listOf(
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 6", false),
            ChecklistNewRecyclerItem(),
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 5", true)
        )
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }

    @Test
    fun checkedItemsAndUncheckedItemsAndNewItemsTest() {
        val inputItems = listOf(
            ChecklistNewRecyclerItem(),
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 5", true),
            ChecklistRecyclerItem("Item 6", false),
            ChecklistNewRecyclerItem()
        )
        val expectedItems = listOf(
            ChecklistRecyclerItem("Item 2", false),
            ChecklistRecyclerItem("Item 4", false),
            ChecklistRecyclerItem("Item 6", false),
            ChecklistNewRecyclerItem(),
            ChecklistNewRecyclerItem(),
            ChecklistRecyclerItem("Item 1", true),
            ChecklistRecyclerItem("Item 3", true),
            ChecklistRecyclerItem("Item 5", true)
        )
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator)

        Assert.assertEquals(expectedItems, actualItems)
    }
}
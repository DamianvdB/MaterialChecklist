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
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.util.DefaultRecyclerItemComparator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DefaultRecyclerItemComparatorTest {

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
        val expectedItems = inputItems.resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        ).resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        val expectedItems = inputItems.resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        ).resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        ).resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        ).resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
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
        ).resetIds()
        val actualItems = inputItems.sortedWith(DefaultRecyclerItemComparator).resetIds()

        Assertions.assertEquals(expectedItems, actualItems)
    }
}
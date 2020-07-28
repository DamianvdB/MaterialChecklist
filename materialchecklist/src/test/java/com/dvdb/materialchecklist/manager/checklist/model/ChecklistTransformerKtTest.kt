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

package com.dvdb.materialchecklist.manager.checklist.model

import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.checklistnew.model.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.util.resetIds
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class ChecklistTransformerKtTest {

    @Nested
    inner class TransformToRecyclerItems {

        @Test
        fun `given empty formatted text then transforms to two items`() {
            val expectedRecyclerItems = listOf(
                ChecklistRecyclerItem(""),
                ChecklistNewRecyclerItem()
            ).resetIds()

            val actualRecyclerItems: List<BaseRecyclerItem> = ChecklistItemContainer(
                id = 1,
                formattedText = ""
            ).transform()
                .resetIds()

            Assertions.assertEquals(
                expectedRecyclerItems,
                actualRecyclerItems
            )
        }

        @Test
        fun `given formatted text representing one unchecked checklist item then transforms to two items`() {
            val expectedRecyclerItems = listOf(
                ChecklistRecyclerItem(text = "Buy brandy", isChecked = false),
                ChecklistNewRecyclerItem()
            ).resetIds()

            val actualRecyclerItems: List<BaseRecyclerItem> = ChecklistItemContainer(
                id = 1,
                formattedText = "[ ] Buy brandy\n"
            ).transform()
                .resetIds()

            Assertions.assertEquals(
                expectedRecyclerItems,
                actualRecyclerItems
            )
        }

        @Test
        fun `given formatted text representing one checked checklist item then transforms to two items`() {
            val expectedRecyclerItems = listOf(
                ChecklistNewRecyclerItem(),
                ChecklistRecyclerItem(text = "Buy brandy", isChecked = true)
            ).resetIds()

            val actualRecyclerItems: List<BaseRecyclerItem> = ChecklistItemContainer(
                id = 1,
                formattedText = "[X] Buy brandy\n"
            ).transform()
                .resetIds()

            Assertions.assertEquals(
                expectedRecyclerItems,
                actualRecyclerItems
            )
        }

        @Test
        fun `given sorted formatted text representing three items then transforms to four items`() {
            val expectedRecyclerItems = listOf(
                ChecklistRecyclerItem(text = "Buy brandy", isChecked = false),
                ChecklistNewRecyclerItem(),
                ChecklistRecyclerItem(text = "Buy beer", isChecked = true),
                ChecklistRecyclerItem(text = "Buy steak", isChecked = true)
            ).resetIds()

            val actualRecyclerItems: List<BaseRecyclerItem> = ChecklistItemContainer(
                id = 1,
                formattedText = "[ ] Buy brandy\n" +
                        "[x] Buy beer\n" +
                        "[X] Buy steak"
            ).transform()
                .resetIds()

            Assertions.assertEquals(
                expectedRecyclerItems,
                actualRecyclerItems
            )
        }

        @Test
        fun `given unsorted formatted text representing three items then transforms to four items`() {
            val expectedRecyclerItems = listOf(
                ChecklistRecyclerItem(text = "Buy brandy", isChecked = false),
                ChecklistNewRecyclerItem(),
                ChecklistRecyclerItem(text = "Buy beer", isChecked = true),
                ChecklistRecyclerItem(text = "Buy steak", isChecked = true)
            ).resetIds()

            val actualRecyclerItems: List<BaseRecyclerItem> = ChecklistItemContainer(
                id = 1,
                formattedText = "[x] Buy beer\n" +
                        "[x] Buy steak\n" +
                        "[ ] Buy brandy"
            ).transform()
                .resetIds()

            Assertions.assertEquals(
                expectedRecyclerItems,
                actualRecyclerItems
            )
        }
    }
}
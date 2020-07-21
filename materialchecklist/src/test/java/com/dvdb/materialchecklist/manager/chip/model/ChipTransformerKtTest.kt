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

package com.dvdb.materialchecklist.manager.chip.model

import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipRecyclerItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ChipTransformerKtTest {

    @Test
    fun transformToChipContainerRecyclerItem() {
        val expectedChipContainerRecyclerItem = ChipContainerRecyclerItem(
            1,
            items = listOf(ChipRecyclerItem(1, "test"))
        )

        val actualChipContainerRecyclerItem: ChipContainerRecyclerItem = ChipItemContainer(
            id = expectedChipContainerRecyclerItem.id.toInt(),
            items = expectedChipContainerRecyclerItem.items.map { it.transform() }
        ).transform()

        Assertions.assertEquals(
            expectedChipContainerRecyclerItem,
            actualChipContainerRecyclerItem
        )
    }

    @Test
    fun transformToChipItemContainer() {
        val expectedChipItemContainer = ChipItemContainer(
            1,
            items = listOf(ChipItem(1, "test"))
        )

        val actualChipItemContainer: ChipItemContainer = ChipContainerRecyclerItem(
            id = expectedChipItemContainer.id.toLong(),
            items = expectedChipItemContainer.items.map { it.transform() }
        ).transform()

        Assertions.assertEquals(
            expectedChipItemContainer,
            actualChipItemContainer
        )
    }

    @Test
    fun transformToChipRecyclerItem() {
        val expectedChipRecyclerItem = ChipRecyclerItem(
            1,
            "test"
        )

        val actualChipRecyclerItem: ChipRecyclerItem = ChipItem(
            id = expectedChipRecyclerItem.id,
            text = expectedChipRecyclerItem.text
        ).transform()

        Assertions.assertEquals(
            expectedChipRecyclerItem,
            actualChipRecyclerItem
        )
    }

    @Test
    fun transformToChipItem() {
        val expectedChipItem = ChipItem(
            1,
            "test"
        )

        val actualChipItem: ChipItem = ChipRecyclerItem(
            id = expectedChipItem.id,
            text = expectedChipItem.text
        ).transform()

        Assertions.assertEquals(
            expectedChipItem,
            actualChipItem
        )
    }
}
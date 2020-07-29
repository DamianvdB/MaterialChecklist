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

package com.dvdb.materialchecklist.manager.title.model

import com.dvdb.materialchecklist.manager.model.TitleItem
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.random.Random

internal class TitleTransformerKtTest {

    @Test
    fun transformToTitleRecyclerItem() {
        val expectedRecyclerItem = TitleRecyclerItem(
            1,
            "test"
        )

        val actualRecyclerItem: TitleRecyclerItem = TitleItem(
            id = Random(0).nextInt(),
            text = expectedRecyclerItem.text
        ).transform()

        Assertions.assertEquals(
            expectedRecyclerItem.text,
            actualRecyclerItem.text
        )
    }

    @Test
    fun transformToTitleItem() {
        val expectedTitleItem = TitleItem(
            1,
            "test"
        )

        val actualTitleItem: TitleItem = TitleRecyclerItem(
            id = expectedTitleItem.id.toLong(),
            text = expectedTitleItem.text
        ).transform()

        Assertions.assertEquals(
            expectedTitleItem,
            actualTitleItem
        )
    }
}
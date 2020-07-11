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

package com.dvdb.materialchecklist.manager.title.util

import com.dvdb.materialchecklist.manager.util.RecyclerItemPositionTracker
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.checklistnew.model.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class TitleRecyclerItemPositionTrackerTest {

    private val itemPositionTracker: RecyclerItemPositionTracker =
        TitleRecyclerItemPositionTracker { items }

    private val items = mutableListOf<BaseRecyclerItem>()

    @Nested
    inner class GetItemCount {

        @Test
        fun `given get item count called and is empty then returns zero`() {
            items.clear()

            assert(itemPositionTracker.itemCount == 0)
        }

        @Test
        fun `given get item count called and contains non-title items then returns zero`() {
            items.clear()
            items.add(ChecklistRecyclerItem("1"))
            items.add(ChecklistRecyclerItem("2"))
            items.add(ChecklistNewRecyclerItem())

            assert(itemPositionTracker.itemCount == 0)
        }

        @Test
        fun `given get item count called and contains two title items then returns two`() {
            items.clear()
            items.add(TitleRecyclerItem(""))
            items.add(TitleRecyclerItem(""))

            assert(itemPositionTracker.itemCount == 2)
        }
    }

    @Nested
    inner class GetFirstItemPosition {

        @Test
        fun `given get first item position called and is empty then returns zero`() {
            items.clear()

            assert(itemPositionTracker.firstItemPosition == 0)
        }

        @Test
        fun `given get first item position called and only contains checklist items then returns zero`() {
            items.clear()
            items.add(ChecklistRecyclerItem("1"))
            items.add(ChecklistRecyclerItem("2"))
            items.add(ChecklistNewRecyclerItem())

            assert(itemPositionTracker.firstItemPosition == 0)
        }

        @Test
        fun `given get first item position called and contains one title item then returns zero`() {
            items.clear()
            items.add(TitleRecyclerItem(""))

            assert(itemPositionTracker.firstItemPosition == 0)
        }
    }

    @Nested
    inner class GetLastItemPosition {

        @Test
        fun `given get last item position called and is empty then returns zero`() {
            items.clear()

            assert(itemPositionTracker.lastItemPosition == 0)
        }

        @Test
        fun `given get last item position called and only contains checklist items then returns zero`() {
            items.clear()
            items.add(ChecklistRecyclerItem("1"))
            items.add(ChecklistRecyclerItem("2"))
            items.add(ChecklistNewRecyclerItem())

            assert(itemPositionTracker.lastItemPosition == 0)
        }

        @Test
        fun `given get last item position called and contains one title item then returns zero`() {
            items.clear()
            items.add(TitleRecyclerItem(""))

            assert(itemPositionTracker.lastItemPosition == 0)
        }
    }
}
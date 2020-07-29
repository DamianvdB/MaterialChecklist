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

import com.dvdb.materialchecklist.manager.model.ChecklistItemContainer
import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.checklistnew.model.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.recycler.util.DefaultRecyclerItemComparator
import com.dvdb.materialchecklist.recycler.util.RecyclerItemMapper

internal fun ChecklistItemContainer.transform(): List<BaseRecyclerItem> {
    val unsortedItems = RecyclerItemMapper.toItems(formattedText)

    return if (unsortedItems.isNotEmpty()) {
        unsortedItems.filterIsInstance<ChecklistRecyclerItem>()
    } else {
        listOf(ChecklistRecyclerItem(""))
    }.plus(ChecklistNewRecyclerItem())
        .sortedWith(DefaultRecyclerItemComparator)
}
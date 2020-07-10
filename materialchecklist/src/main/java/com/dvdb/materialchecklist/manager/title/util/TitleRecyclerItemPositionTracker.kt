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
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.title.TitleRecyclerItem

internal class TitleRecyclerItemPositionTracker(
    private val items: (() -> List<BaseRecyclerItem>)
) : RecyclerItemPositionTracker {

    override val itemCount: Int
        get() = items().count(predicate)

    override val firstItemPosition: Int
        get() = items().indexOfFirst(predicate)
            .coerceAtLeast(0)

    override val lastItemPosition: Int
        get() = items().indexOfLast(predicate)
            .coerceAtLeast(0)

    private val predicate: (BaseRecyclerItem) -> Boolean = { it is TitleRecyclerItem }
}
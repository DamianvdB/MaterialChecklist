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

package com.dvdb.materialchecklist.manager.content

import com.dvdb.materialchecklist.recycler.base.model.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerItem

internal class ContentManagerImpl : ContentManager {

    private lateinit var items: () -> List<BaseRecyclerItem>
    private lateinit var updateItemSilently: (item: BaseRecyclerItem, position: Int) -> Unit

    private var hasFocus: Boolean = false

    override fun lateInitContentState(
        items: () -> List<BaseRecyclerItem>,
        updateItemSilently: (item: BaseRecyclerItem, position: Int) -> Unit
    ) {
        this.items = items
        this.updateItemSilently = updateItemSilently
    }

    override fun onContentItemTextChanged(
        position: Int,
        text: String
    ) {
        val item = items().getOrNull(position)

        if (item is ContentRecyclerItem) {
            updateItemSilently(
                item.copy(text = text),
                position
            )
        }
    }

    override fun onContentItemFocusChanged(
        position: Int,
        startSelection: Int,
        endSelection: Int,
        hasFocus: Boolean
    ) {
        this.hasFocus = hasFocus
    }
}
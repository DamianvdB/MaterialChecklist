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

package com.dvdb.materialchecklist.recycler.base.adapter

import androidx.recyclerview.widget.RecyclerView

@Suppress("PropertyName")
internal abstract class BaseRecyclerAdapter<T>(
    items: List<T>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected var _items: MutableList<T> = items.toMutableList()

    override fun getItemCount() = _items.size

    val items: List<T>
        get() = _items.toList()

    fun setItems(
        items: List<T>,
        notify: Boolean = true
    ) {
        _items = items.toMutableList()
        if (notify) {
            notifyDataSetChanged()
        }
    }

    fun addItem(
        item: T,
        position: Int,
        notify: Boolean = true
    ) {
        _items.add(position, item)
        if (notify) {
            notifyItemInserted(position)
        }
    }

    fun removeItem(
        position: Int,
        notify: Boolean = true
    ) {
        _items.removeAt(position)
        if (notify) {
            notifyItemRemoved(position)
        }
    }

    fun updateItem(
        item: T,
        position: Int,
        notify: Boolean = true
    ) {
        _items[position] = item
        if (notify) {
            notifyItemChanged(position)
        }
    }
}
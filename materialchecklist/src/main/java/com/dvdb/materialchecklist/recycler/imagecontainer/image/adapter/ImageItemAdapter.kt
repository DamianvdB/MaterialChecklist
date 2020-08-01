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

package com.dvdb.materialchecklist.recycler.imagecontainer.image.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.recycler.imagecontainer.image.holder.ImageRecyclerHolder
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerItem
import com.dvdb.materialchecklist.recycler.util.SimpleItemDiffCallback

internal class ImageItemAdapter(
    private val itemImageRecyclerHolderFactory: ImageRecyclerHolder.Factory,
    config: ImageRecyclerHolderConfig,
    items: List<ImageRecyclerItem> = emptyList()
) : RecyclerView.Adapter<ImageRecyclerHolder>() {

    private val _items: MutableList<ImageRecyclerItem> = items.toMutableList()
    val items: List<ImageRecyclerItem>
        get() = _items

    var config: ImageRecyclerHolderConfig = config
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageRecyclerHolder {
        return itemImageRecyclerHolderFactory.create(
            parent,
            config
        )
    }

    override fun onBindViewHolder(holder: ImageRecyclerHolder, position: Int) {
        val item = _items[position]
        holder.updateConfigConditionally(config)
        holder.bindView(item)
    }

    override fun getItemCount(): Int = _items.size

    fun submitItems(items: List<ImageRecyclerItem>) {
        val diffCallback = SimpleItemDiffCallback(
            oldItems = this._items,
            newItems = items,
            areTheSame = { left, right -> left.id == right.id },
            areContentsTheSame = { left, right -> left == right }
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this._items.clear()
        this._items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
}
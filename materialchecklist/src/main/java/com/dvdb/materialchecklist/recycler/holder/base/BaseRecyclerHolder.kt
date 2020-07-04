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

package com.dvdb.materialchecklist.recycler.holder.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem

internal abstract class BaseRecyclerHolder<T : BaseRecyclerItem, C : BaseRecyclerHolderConfig>(
    itemView: View,
    protected var config: C
) : RecyclerView.ViewHolder(itemView) {

    fun updateConfigConditionally(config: C) {
        if (this.config != config) {
            this.config = config
            onConfigUpdated()
        }
    }

    abstract fun bindView(item: T)

    abstract fun onConfigUpdated()
}

internal interface BaseRecyclerHolderConfig : Config
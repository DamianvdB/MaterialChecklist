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

package com.dvdb.materialchecklist.recycler.holder.checklistnew

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.materialchecklist.util.setTintCompat
import kotlinx.android.synthetic.main.item_checklist_new.view.*

internal class ChecklistNewRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistNewRecyclerHolderConfig,
    private val onItemClicked: (position: Int) -> Unit
) : BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig>(itemView, config) {

    private val addIcon: ImageView = itemView.item_checklist_new_add
    private val text: TextView = itemView.item_checklist_new_text

    init {
        initialiseView()
    }

    override fun bindView(item: ChecklistNewRecyclerItem) {
    }

    override fun onConfigUpdated() {
        initialiseView()
    }

    private fun initialiseView() {
        initialiseRoot()
        initialiseAdd()
        initialiseText()
    }

    private fun initialiseRoot() {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }

        config.leftAndRightPadding?.toInt()?.let { padding ->
            itemView.setPadding(
                padding,
                itemView.paddingTop,
                padding,
                itemView.paddingBottom
            )
        }
    }

    private fun initialiseAdd() {
        val tintedIcon = config.iconAdd
        tintedIcon?.setTintCompat(config.iconTintColor)
        addIcon.setImageDrawable(tintedIcon)

        addIcon.alpha = config.iconAlphaAdd
    }

    private fun initialiseText() {
        text.text = config.text
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.textSize)
        text.setTextColor(config.textColor)
        text.alpha = config.textAlpha

        config.textTypeFace?.let {
            text.typeface = it
        }
    }

    class Factory(
        private val onItemClicked: (position: Int) -> Unit
    ) : BaseRecyclerHolderFactory<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChecklistNewRecyclerHolderConfig
        ): BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig> {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_checklist_new,
                parent,
                false
            )
            return ChecklistNewRecyclerHolder(
                itemView,
                config,
                onItemClicked
            )
        }
    }
}
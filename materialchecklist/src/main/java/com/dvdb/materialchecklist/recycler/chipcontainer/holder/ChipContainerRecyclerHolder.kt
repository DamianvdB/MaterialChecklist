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

package com.dvdb.materialchecklist.recycler.chipcontainer.holder

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.ViewGroup
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipContainerRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.chipcontainer.model.ChipRecyclerItem
import com.dvdb.materialchecklist.util.getDrawableCompat
import com.dvdb.materialchecklist.util.setTintCompat
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

internal class ChipContainerRecyclerHolder private constructor(
    private val chipGroup: ChipGroup,
    config: ChipContainerRecyclerHolderConfig,
    private val onItemClicked: (id: Int) -> Unit
) : BaseRecyclerHolder<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig>(
    chipGroup,
    config
) {

    init {
        initialiseChipGroup()
    }

    override fun bindView(item: ChipContainerRecyclerItem) {
        chipGroup.removeAllViews()

        item.items.forEach { chipItem ->
            chipGroup.addView(createChip(chipItem))
        }
    }

    override fun onConfigUpdated() {
        initialiseChipGroup()
    }

    private fun createChip(chipItem: ChipRecyclerItem): Chip {
        val chip = Chip(chipGroup.context)

        chip.text = chipItem.text
        chip.typeface = config.textTypeFace
        chip.setTextColor(config.textColor)

        chip.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            config.textSize
        )

        if (chipItem.iconRes != -1) {
            val drawable = chipGroup.context.getDrawableCompat(chipItem.iconRes)
            drawable?.setTintCompat(config.iconTintColor)
            chip.chipIcon = drawable
        }

        chip.chipIconSize = config.iconSize

        config.iconEndPadding?.let {
            chip.iconEndPadding = it
        }

        config.backgroundColor?.let {
            chip.chipBackgroundColor = ColorStateList.valueOf(it)
        }

        config.strokeColor?.let {
            chip.chipStrokeColor = ColorStateList.valueOf(it)
        }

        config.strokeWidth?.let {
            chip.chipStrokeWidth = it
        }

        chip.chipStartPadding = config.leftAndRightInternalPadding
        chip.chipEndPadding = config.leftAndRightInternalPadding

        chip.chipMinHeight = config.minHeight

        chip.setOnClickListener {
            onItemClicked(chipItem.id)
        }

        return chip
    }

    private fun initialiseChipGroup() {
        val topAndBottomPadding = config.topAndBottomPadding?.toInt() ?: itemView.paddingTop
        val leftAndRightPadding = config.leftAndRightPadding?.toInt() ?: itemView.paddingLeft
        chipGroup.setPadding(
            leftAndRightPadding,
            topAndBottomPadding,
            leftAndRightPadding,
            topAndBottomPadding
        )

        chipGroup.chipSpacingHorizontal = config.horizontalSpacing
    }

    class Factory(
        private val onItemClicked: (id: Int) -> Unit
    ) : BaseRecyclerHolderFactory<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChipContainerRecyclerHolderConfig
        ): BaseRecyclerHolder<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig> {
            return ChipContainerRecyclerHolder(
                ChipGroup(parent.context),
                config,
                onItemClicked
            )
        }
    }
}
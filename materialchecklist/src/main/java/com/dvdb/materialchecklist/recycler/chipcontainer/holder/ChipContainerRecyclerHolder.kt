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
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

internal class ChipContainerRecyclerHolder private constructor(
    private val chipGroup: ChipGroup,
    config: ChipContainerRecyclerHolderConfig,
    private val onItemClicked: (item: ChipRecyclerItem) -> Unit,
    private val onItemLongClicked: (item: ChipRecyclerItem) -> Boolean
) : BaseRecyclerHolder<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig>(
    chipGroup,
    config
) {

    init {
        initialiseChipGroup()
    }

    override fun bindView(item: ChipContainerRecyclerItem) {
        if (item.items.size != chipGroup.childCount) {
            chipGroup.removeAllViews()

            item.items.forEach { chipItem ->
                chipGroup.addView(createChip(chipItem))
            }
        } else {
            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as? Chip

                item.items.getOrNull(i)?.let { chipItem ->
                    chip?.bindItem(chipItem)
                }
            }
        }
    }

    override fun onConfigUpdated() {
        initialiseChipGroup()

        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.updateAppearance()
        }
    }

    private fun createChip(chipItem: ChipRecyclerItem): Chip {
        val chip = Chip(chipGroup.context)

        chip.bindItem(chipItem)
        chip.updateAppearance()

        return chip
    }

    private fun Chip.bindItem(chipItem: ChipRecyclerItem) {
        text = chipItem.text

        if (chipItem.iconRes != -1) {
            chipIcon = chipGroup.context.getDrawableCompat(chipItem.iconRes)
            chipIconTint = ColorStateList.valueOf(config.iconTintColor)
        }

        setOnClickListener {
            onItemClicked(chipItem)
        }

        setOnLongClickListener {
            onItemLongClicked(chipItem)
        }
    }

    private fun Chip.updateAppearance() {
        typeface = config.textTypeFace
        setTextColor(config.textColor)

        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            config.textSize
        )

        chipIconSize = config.iconSize

        config.iconEndPadding?.let {
            iconEndPadding = it
        }

        config.backgroundColor?.let {
            chipBackgroundColor = ColorStateList.valueOf(it)
        }

        config.strokeColor?.let {
            chipStrokeColor = ColorStateList.valueOf(it)
        }

        config.strokeWidth?.let {
            chipStrokeWidth = it
        }

        chipStartPadding = config.leftAndRightInternalPadding
        chipEndPadding = config.leftAndRightInternalPadding

        chipMinHeight = config.minHeight
    }

    private fun initialiseChipGroup() {
        val topAndBottomPadding = config.topAndBottomPadding.toInt()
        val leftAndRightPadding = config.leftAndRightPadding.toInt()

        chipGroup.setPadding(
            leftAndRightPadding,
            topAndBottomPadding,
            leftAndRightPadding,
            topAndBottomPadding
        )

        chipGroup.chipSpacingHorizontal = config.horizontalSpacing
    }

    class Factory(
        private val onItemClicked: (item: ChipRecyclerItem) -> Unit,
        private val onItemLongClicked: (item: ChipRecyclerItem) -> Boolean
    ) : BaseRecyclerHolderFactory<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChipContainerRecyclerHolderConfig
        ): BaseRecyclerHolder<ChipContainerRecyclerItem, ChipContainerRecyclerHolderConfig> {
            return ChipContainerRecyclerHolder(
                ChipGroup(parent.context),
                config,
                onItemClicked,
                onItemLongClicked
            )
        }
    }
}
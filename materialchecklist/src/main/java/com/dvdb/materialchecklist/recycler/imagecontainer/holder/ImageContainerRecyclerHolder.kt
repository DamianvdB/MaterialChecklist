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

package com.dvdb.materialchecklist.recycler.imagecontainer.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.imagecontainer.image.adapter.ImageItemAdapter
import com.dvdb.materialchecklist.recycler.imagecontainer.image.holder.ImageRecyclerHolder
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerItem
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerItem
import com.dvdb.materialchecklist.recycler.util.RecyclerGridSpaceItemDecorator
import com.dvdb.materialchecklist.util.isTablet
import com.dvdb.materialchecklist.util.updatePadding

private const val MIN_COLUMN_SPAN = 1
private const val MIN_TEXT_LINES = 1
private const val MAX_COLUMN_SPAN_FOR_TWO_TEXT_LINES = 4

internal class ImageContainerRecyclerHolder private constructor(
    private val recyclerView: RecyclerView,
    config: ImageContainerRecyclerHolderConfig,
    private val onItemClicked: (item: ImageRecyclerItem) -> Unit,
    private val onItemLongClicked: (item: ImageRecyclerItem) -> Boolean
) : BaseRecyclerHolder<ImageContainerRecyclerItem, ImageContainerRecyclerHolderConfig>(
    recyclerView,
    config
) {
    private val isTablet: Boolean = itemView.context.isTablet()

    init {
        initView()
        initRecyclerView()
    }

    private val textSizeForColumnSpanTwo =
        itemView.context.resources.getDimension(R.dimen.mc_item_image_text_size_span_count_two)

    private val textSizeForColumnSpanThree =
        itemView.context.resources.getDimension(R.dimen.mc_item_image_text_size_span_count_three)

    private val textSizeForColumnSpanFour =
        itemView.context.resources.getDimension(R.dimen.mc_item_image_text_size_span_count_four)

    private val textSizeForColumnSpanGreaterThanFour =
        itemView.context.resources.getDimension(R.dimen.mc_item_image_text_size_span_count_min)

    override fun bindView(item: ImageContainerRecyclerItem) {
        (recyclerView.adapter as? ImageItemAdapter)?.submitItems(item.items)

        if (!isTablet) {
            val columnSpan = calculateItemColumnSpan(item.items.size)
            (recyclerView.layoutManager as? GridLayoutManager)?.spanCount = columnSpan

            adjustItemTextConfig(columnSpan)
        }
        updateTopAndBottomPadding()
    }

    override fun onConfigUpdated() {
        initView()
    }

    private fun initView() {
        initRoot()

        if (!isTablet) {
            val columnSpan =
                calculateItemColumnSpan((recyclerView.adapter as? ImageItemAdapter)?.itemCount ?: 0)
            (recyclerView.layoutManager as? GridLayoutManager)?.spanCount = columnSpan

            adjustItemTextConfig(columnSpan)
        } else {
            (recyclerView.layoutManager as? GridLayoutManager)?.spanCount = config.maxColumnSpan
        }

        if (recyclerView.itemDecorationCount > 0) {
            val itemDecorator =
                recyclerView.getItemDecorationAt(0) as? RecyclerGridSpaceItemDecorator

            if (itemDecorator != null) {
                itemDecorator.spacing = config.innerPadding.toInt()
            }
        }
    }

    private fun initRoot() {
        val leftAndRightPadding = config.leftAndRightPadding?.toInt() ?: itemView.paddingLeft

        itemView.updatePadding(
            left = leftAndRightPadding,
            right = leftAndRightPadding
        )
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(
            recyclerView.context,
            config.maxColumnSpan
        )

        fun getItemForPosition(position: Int): ImageRecyclerItem? {
            return (recyclerView.adapter as? ImageItemAdapter)?.items
                ?.getOrNull(position)
        }

        recyclerView.adapter = ImageItemAdapter(
            ImageRecyclerHolder.Factory(
                onItemClicked = {
                    getItemForPosition(it)?.let { item ->
                        onItemClicked(item)
                    }
                },
                onItemLongClicked = {
                    getItemForPosition(it)?.let { item ->
                        onItemLongClicked(item)
                    } ?: false
                }
            ),
            config.imageItemConfig
        )

        if (recyclerView.itemDecorationCount == 0) {
            recyclerView.addItemDecoration(
                RecyclerGridSpaceItemDecorator(spacing = config.innerPadding.toInt())
            )
        }
    }

    private fun calculateItemColumnSpan(itemCount: Int): Int {
        return itemCount.coerceIn(
            MIN_COLUMN_SPAN,
            config.maxColumnSpan
        )
    }

    private fun adjustItemTextConfig(columnSpan: Int) {
        if (config.adjustItemTextSize) {
            val maxTextLines = if (columnSpan <= MAX_COLUMN_SPAN_FOR_TWO_TEXT_LINES) {
                config.imageItemConfig.maxTextLines
            } else {
                MIN_TEXT_LINES
            }

            (recyclerView.adapter as? ImageItemAdapter)?.config = config.imageItemConfig.copy(
                textSize = calculateItemTextSize(columnSpan),
                maxTextLines = maxTextLines
            )
        }
    }

    private fun calculateItemTextSize(columnSpan: Int): Float {
        val maxTextSize = config.imageItemConfig.textSize

        return when (columnSpan) {
            1 -> maxTextSize
            2 -> textSizeForColumnSpanTwo
            3 -> textSizeForColumnSpanThree
            MAX_COLUMN_SPAN_FOR_TWO_TEXT_LINES -> textSizeForColumnSpanFour
            else -> textSizeForColumnSpanGreaterThanFour
        }.coerceAtMost(maxTextSize)
    }

    private fun updateTopAndBottomPadding() {
        val topAndBottomPadding = if ((recyclerView.adapter?.itemCount ?: 0) > 0) {
            config.topAndBottomPadding.toInt()
        } else {
            0
        }

        if (itemView.paddingTop != topAndBottomPadding ||
            itemView.paddingBottom != topAndBottomPadding
        ) {
            itemView.updatePadding(
                top = topAndBottomPadding,
                bottom = topAndBottomPadding
            )
        }
    }

    class Factory(
        private val onItemClicked: (item: ImageRecyclerItem) -> Unit,
        private val onItemLongClicked: (item: ImageRecyclerItem) -> Boolean
    ) : BaseRecyclerHolderFactory<ImageContainerRecyclerItem, ImageContainerRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ImageContainerRecyclerHolderConfig
        ): BaseRecyclerHolder<ImageContainerRecyclerItem, ImageContainerRecyclerHolderConfig> {
            return ImageContainerRecyclerHolder(
                createRecyclerView(parent),
                config,
                onItemClicked,
                onItemLongClicked
            )
        }

        private fun createRecyclerView(
            parent: ViewGroup
        ): RecyclerView {
            return RecyclerView(parent.context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }
    }
}
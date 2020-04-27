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

package com.dvdb.materialchecklist.recycler.util

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val NO_ORIENTATION: Int = -1

/**
 * Recycler view item decorator that allows adding offsets to items.
 *
 * @param firstItemMargin The margin in pixels to add to the top (vertical orientation ) or the left (horizontal orientation) of the first item.
 * @param lastItemMargin The margin in pixels to add to the bottom (vertical orientation ) or the right (horizontal orientation) of the last item.
 * @param sideMargin The margin in pixels to add to the left and the right (vertical orientation) or to the top and the bottom (horizontal orientation) of all items.
 */
internal class RecyclerSpaceItemDecorator(
    @Px private val firstItemMargin: Int = 0,
    @Px private val lastItemMargin: Int = 0,
    @Px private val sideMargin: Int = 0
) : RecyclerView.ItemDecoration() {

    private var orientation: Int = NO_ORIENTATION

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }

        if (orientation == NO_ORIENTATION) {
            orientation = getOrientation(parent)
        }

        if (orientation == LinearLayoutManager.VERTICAL) {
            setVerticalItemOffsets(position, outRect, state)
        } else {
            setHorizontalItemOffsets(position, outRect, state)
        }

        if (firstItemMargin != 0 && parent.itemAnimator !is RecyclerSpaceItemDecoratorAnimation) {
            parent.itemAnimator = RecyclerSpaceItemDecoratorAnimation(
                firstItemOffset = firstItemMargin,
                orientation = orientation
            ) {
                parent.adapter?.itemCount?.minus(1) ?: 0
            }
        }
    }

    private fun setVerticalItemOffsets(
        position: Int,
        outRect: Rect,
        state: RecyclerView.State
    ) {
        outRect.run {
            if (position == 0) {
                if (firstItemMargin > 0) {
                    top = firstItemMargin
                }
            } else {
                if (position == state.lastIndex && lastItemMargin > 0) {
                    bottom = lastItemMargin
                }
            }

            if (sideMargin > 0) {
                left = sideMargin
                right = sideMargin
            }
        }
    }

    private fun setHorizontalItemOffsets(
        position: Int,
        outRect: Rect,
        state: RecyclerView.State
    ) {
        outRect.run {
            if (position == 0) {
                if (firstItemMargin > 0) {
                    left = firstItemMargin
                }

            } else {
                if (position == state.lastIndex && lastItemMargin > 0) {
                    right = lastItemMargin
                }
            }

            if (sideMargin > 0) {
                top = sideMargin
                bottom = sideMargin
            }
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        return (parent.layoutManager as? LinearLayoutManager)?.orientation
            ?: error("${this.javaClass.simpleName} can only be used with a LinearLayoutManager")
    }

    private val RecyclerView.State.lastIndex: Int
        get() = itemCount - 1
}

/**
 * Recycler view item animator to account for the first item top (vertical orientation) or
 * left offset (horizontal orientation) on performing the item removal animation.
 *
 * @param firstItemOffset The offset in pixels added to the top (vertical orientation) or the left (horizontal orientation) of the first item.
 * @param orientation The orientation of the recycler view.
 * @param lastItemIndex The function to return the index of the last item.
 */
private class RecyclerSpaceItemDecoratorAnimation(
    @Px private val firstItemOffset: Int,
    private val orientation: Int,
    private val lastItemIndex: () -> Int
) : DefaultItemAnimator() {

    init {
        require(orientation == RecyclerView.VERTICAL || orientation == RecyclerView.HORIZONTAL) {
            "Orientation must either be 'RecyclerView.VERTICAL' or 'RecyclerView.HORIZONTAL'"
        }
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        return if (holder.layoutPosition == RecyclerView.NO_POSITION || holder.layoutPosition == lastItemIndex()) {
            if (orientation == RecyclerView.VERTICAL) {
                ViewCompat.offsetTopAndBottom(holder.itemView, firstItemOffset - holder.itemView.top)
            } else {
                ViewCompat.offsetLeftAndRight(holder.itemView, firstItemOffset - holder.itemView.left)
            }
            super.animateRemove(holder)
            true
        } else {
            super.animateRemove(holder)
        }
    }
}
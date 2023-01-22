/*
 * Designed and developed by Damian van den Berg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

internal class RecyclerGridSpaceItemDecorator(
    @Px var spacing: Int = 0,
    private var includeEdge: Boolean = false,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: return
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        if (includeEdge) {
            outRect.left = spacing - ((column * spacing) / spanCount)

            outRect.right = ((column + 1) * spacing) / spanCount

            if (position < spanCount) { // first row
                outRect.top = spacing
            }

            outRect.bottom = spacing

        } else {
            outRect.left = (column * spacing) / spanCount

            outRect.right = spacing - (((column + 1) * spacing) / spanCount)

            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}
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

package com.dvdb.materialchecklist.config.chip.model

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.chip.model.ChipContainerRecyclerHolderConfig

internal class ChipConfig(
    context: Context,
    val textColor: () -> Int,
    val textSize: () -> Float,
    val typeFace: () -> Typeface?,
    val iconTintColor: () -> Int,
    val leftAndRightPadding: () -> Float,
    val topAndBottomPadding: () -> Float,
    @ColorInt var backgroundColor: Int? = null,
    @ColorInt var strokeColor: Int? = null,
    @Px var strokeWidth: Float? = null,
    @Px var iconSize: Float = context.resources.getDimension(R.dimen.mc_icon_size_small),
    @Px var iconEndPadding: Float? = null,
    @Px var minHeight: Float = context.resources.getDimension(R.dimen.mc_item_chip_min_height),
    @Px var horizontalSpacing: Int = context.resources.getDimensionPixelSize(R.dimen.mc_spacing_medium_large),
    @Px var leftAndRightInternalPadding: Float = context.resources.getDimension(R.dimen.mc_spacing_medium)
) : Config {

    fun transform() = ChipContainerRecyclerHolderConfig(
        textColor = textColor(),
        textSize = textSize(),
        textTypeFace = typeFace(),
        iconTintColor = iconTintColor(),
        iconSize = iconSize,
        iconEndPadding = iconEndPadding,
        backgroundColor = backgroundColor,
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        minHeight = minHeight,
        horizontalSpacing = horizontalSpacing,
        leftAndRightInternalPadding = leftAndRightInternalPadding,
        topAndBottomPadding = topAndBottomPadding(),
        leftAndRightPadding = leftAndRightPadding()
    )
}
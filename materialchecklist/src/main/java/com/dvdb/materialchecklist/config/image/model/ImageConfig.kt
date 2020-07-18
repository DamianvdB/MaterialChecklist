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

package com.dvdb.materialchecklist.config.image.model

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.imagecontainer.model.ImageContainerRecyclerHolderConfig
import com.dvdb.materialchecklist.util.getColorCompat

internal class ImageConfig(
    context: Context,
    var maxColumnSpan: Int = context.resources.getInteger(R.integer.mc_item_image_container_column_span),
    @ColorInt var textColor: Int? = null,
    @ColorInt var strokeColor: Int = context.getColorCompat(R.color.mc_image_container_stroke),
    @Px var strokeWidth: Int = context.resources.getDimensionPixelSize(R.dimen.mc_item_image_stroke_width),
    var textSize: Float = context.resources.getDimension(R.dimen.mc_item_image_text_size),
    var cornerRadius: Float = context.resources.getDimension(R.dimen.mc_item_image_corner_radius),
    private val maxTextLines: Int = context.resources.getInteger(R.integer.mc_item_image_text_max_lines),
    var innerPadding: Float = context.resources.getDimension(R.dimen.mc_item_image_inner_margin),
    var leftAndRightPadding: Float? = null,
    var topAndBottomPadding: Float? = null,
    var adjustItemTextSize: Boolean = true,
    private val _textColor: () -> Int,
    private val typeFace: () -> Typeface?,
    private val _leftAndRightPadding: () -> Float?,
    private val _topAndBottomPadding: () -> Float
) : Config {

    fun transform() = ImageContainerRecyclerHolderConfig(
        maxColumnSpan = maxColumnSpan,
        innerPadding = innerPadding,
        leftAndRightPadding = leftAndRightPadding ?: _leftAndRightPadding(),
        topAndBottomPadding = topAndBottomPadding ?: _topAndBottomPadding(),
        adjustItemTextSize = adjustItemTextSize,
        imageItemConfig = ImageRecyclerHolderConfig(
            textColor = textColor ?: _textColor(),
            strokeColor = strokeColor,
            strokeWidth = strokeWidth,
            cornerRadius = cornerRadius,
            textSize = textSize,
            maxTextLines = maxTextLines,
            typeFace = typeFace()
        )
    )
}
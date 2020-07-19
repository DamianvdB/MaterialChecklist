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

package com.dvdb.materialchecklist.config.title.model

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerHolderConfig
import com.dvdb.materialchecklist.util.getDrawableCompat

internal class TitleConfig(
    context: Context,
    var hint: String = String(),
    @ColorInt var textColor: Int? = null,
    @ColorInt var linkTextColor: Int? = null,
    @ColorInt var hintTextColor: Int? = null,
    var isLinksClickable: Boolean = true,
    var isEditable: Boolean = true,
    var isShowActionIcon: Boolean = false,
    private val actionIcon: Drawable? = context.getDrawableCompat(R.drawable.ic_more_vert_white),
    private val typeFaceStyle: Int = Typeface.BOLD,
    private val _textColor: () -> Int,
    private val textSize: () -> Float,
    private val typeFace: () -> Typeface?,
    private val iconTintColor: () -> Int,
    private val leftAndRightPadding: () -> Float?
) : Config {

    private val leftPaddingOffset: Float =
        context.resources.getDimension(R.dimen.mc_spacing_medium)

    private val textSizeOffset: Float =
        context.resources.getDimension(R.dimen.mc_item_title_text_size_offset)

    fun transform() = TitleRecyclerHolderConfig(
        hint = hint,
        textColor = textColor ?: _textColor(),
        linkTextColor = linkTextColor ?: _textColor(),
        hintTextColor = hintTextColor,
        iconTintColor = iconTintColor(),
        textSize = textSize() + textSizeOffset,
        isLinksClickable = isLinksClickable,
        isEditable = isEditable,
        isShowActionIcon = isShowActionIcon,
        typeFace = typeFace(),
        typeFaceStyle = typeFaceStyle,
        actionIcon = actionIcon,
        leftPadding = (leftAndRightPadding() ?: 0f) + leftPaddingOffset,
        rightPadding = leftAndRightPadding()
    )
}
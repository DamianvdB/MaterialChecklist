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

package com.dvdb.materialchecklist.config.content.model

import android.graphics.Typeface
import androidx.annotation.ColorInt
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerHolderConfig

internal class ContentConfig(
    var hint: String = String(),
    @ColorInt var linkTextColor: Int? = null,
    @ColorInt var hintTextColor: Int? = null,
    var isLinksClickable: Boolean = true,
    private val textColor: () -> Int,
    private val textSize: () -> Float,
    private val typeFace: () -> Typeface?,
    private val isEditable: () -> Boolean,
    private val leftAndRightPadding: () -> Float?,
    private val leftAndRightPaddingOffset: Float
) : Config {

    fun transform() = ContentRecyclerHolderConfig(
        hint = hint,
        textColor = textColor(),
        linkTextColor = linkTextColor ?: textColor(),
        hintTextColor = hintTextColor,
        textSize = textSize(),
        isLinksClickable = isLinksClickable,
        isEditable = isEditable(),
        typeFace = typeFace(),
        leftAndRightPadding = (leftAndRightPadding() ?: 0f) + leftAndRightPaddingOffset
    )
}
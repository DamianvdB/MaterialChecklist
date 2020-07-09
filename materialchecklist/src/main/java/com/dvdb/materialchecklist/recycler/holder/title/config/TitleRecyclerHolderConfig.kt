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

package com.dvdb.materialchecklist.recycler.holder.title.config

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolderConfig

internal class TitleRecyclerHolderConfig(
    val hint: String,
    @ColorInt val textColor: Int,
    @ColorInt val linkTextColor: Int,
    @ColorInt val hintTextColor: Int?,
    @ColorInt val iconTintColor: Int,
    @Px val textSize: Float,
    val isLinksClickable: Boolean,
    val isEditable: Boolean,
    val isShowActionIcon: Boolean,
    val typeFace: Typeface?,
    val typeFaceStyle: Int,
    val actionIcon: Drawable?,
    @Px val leftPadding: Float?,
    @Px val rightPadding: Float?
) : BaseRecyclerHolderConfig {

    private var _isAddClickableLinks: Boolean = true
    val isAddClickableLinks: Boolean
        get() {
            return (isLinksClickable and _isAddClickableLinks).also {
                _isAddClickableLinks = false
            }
        }
}
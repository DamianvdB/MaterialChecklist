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

package com.dvdb.materialchecklist.recycler.content.model

import android.graphics.Typeface
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderConfig

internal class ContentRecyclerHolderConfig(
    val hint: String,
    @ColorInt val textColor: Int,
    @ColorInt val linkTextColor: Int,
    @ColorInt val hintTextColor: Int?,
    @Px val textSize: Float,
    val isLinksClickable: Boolean,
    val isEditable: Boolean,
    val typeFace: Typeface?,
    @Px val itemLeftAndRightPadding: Float?
) : BaseRecyclerHolderConfig
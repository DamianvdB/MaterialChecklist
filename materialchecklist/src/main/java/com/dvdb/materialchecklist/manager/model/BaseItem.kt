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

package com.dvdb.materialchecklist.manager.model

import com.dvdb.materialchecklist.manager.chip.model.ChipItem
import com.dvdb.materialchecklist.manager.image.model.ImageItem
import com.dvdb.materialchecklist.manager.util.model.RequestFocus

sealed class BaseItem {
    abstract val id: Int
}

data class TitleItem(
    override val id: Int,
    val text: String,
    val requestFocus: RequestFocus = RequestFocus.None
) : BaseItem()

data class ContentItem(
    override val id: Int,
    val text: String,
    val requestFocus: RequestFocus = RequestFocus.None
) : BaseItem()

data class ChecklistItemContainer(
    override val id: Int,
    val formattedText: String,
    val requestFocus: RequestFocus = RequestFocus.None
) : BaseItem()

data class ImageItemContainer(
    override val id: Int,
    val items: List<ImageItem>
) : BaseItem()

data class ChipItemContainer(
    override val id: Int,
    val items: List<ChipItem>
) : BaseItem()
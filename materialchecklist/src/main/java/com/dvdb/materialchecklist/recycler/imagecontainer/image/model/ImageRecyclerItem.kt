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

package com.dvdb.materialchecklist.recycler.imagecontainer.image.model

import android.graphics.drawable.Drawable
import android.net.Uri

internal data class ImageRecyclerItem(
    val id: Int,
    val text: String,
    val primaryImage: Drawable?,
    val primaryImageUri: Uri = Uri.EMPTY,
    val secondaryImage: Drawable?,
    val secondaryImageUri: Uri = Uri.EMPTY
) {
    val shouldShowText: Boolean = text.isNotBlank()

    val isPrimaryImageDrawableSet: Boolean = primaryImage != null
    val isPrimaryImageUriSet: Boolean = primaryImageUri != Uri.EMPTY

    val isSecondaryImageDrawableSet: Boolean = secondaryImage != null
    val isSecondaryImageUriSet: Boolean = secondaryImageUri != Uri.EMPTY
}
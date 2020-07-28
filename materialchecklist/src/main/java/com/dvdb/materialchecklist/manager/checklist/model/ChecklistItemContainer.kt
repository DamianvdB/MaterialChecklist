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

package com.dvdb.materialchecklist.manager.checklist.model

import com.dvdb.materialchecklist.manager.base.BaseItem
import com.dvdb.materialchecklist.manager.util.model.RequestFocus

data class ChecklistItemContainer(
    override val id: Int,
    val formattedText: String,
    val requestFocus: RequestFocus = RequestFocus.None
) : BaseItem() {
    override val type: Type = Type.CHECKLIST_CONTAINER
}
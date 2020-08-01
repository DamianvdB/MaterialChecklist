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

import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.config.checklist.model.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.checklist.model.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.checklist.model.DragAndDropDismissKeyboardBehavior
import com.dvdb.materialchecklist.recycler.adapter.model.ChecklistItemAdapterConfig

internal data class ChecklistManagerConfig(
    val dragAndDropEnabled: Boolean,
    val dragAndDropDismissKeyboardBehavior: DragAndDropDismissKeyboardBehavior,
    val behaviorCheckedItem: BehaviorCheckedItem,
    val behaviorUncheckedItem: BehaviorUncheckedItem,
    val itemFirstTopPadding: Float?,
    val itemLastBottomPadding: Float?,
    val adapterConfig: ChecklistItemAdapterConfig
) : Config
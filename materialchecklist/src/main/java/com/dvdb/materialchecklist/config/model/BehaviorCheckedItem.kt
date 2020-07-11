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

package com.dvdb.materialchecklist.config.model

import androidx.annotation.CheckResult
import com.dvdb.materialchecklist.config.model.BehaviorCheckedItem.*

/**
 * The checklist item behavior for when a item is checked.
 *
 * Using [MOVE_TO_TOP_OF_CHECKED_ITEMS] moves the checked checklist item to the top of the other checked items. This is the default behavior.
 * Using [MOVE_TO_BOTTOM_OF_CHECKED_ITEMS] moves the checked checklist item to the bottom of the other checked items.
 * Using [DELETE] deletes the checked checklist item.
 */
enum class BehaviorCheckedItem {
    MOVE_TO_TOP_OF_CHECKED_ITEMS,
    MOVE_TO_BOTTOM_OF_CHECKED_ITEMS,
    DELETE;

    companion object {
        internal val DEFAULT: BehaviorCheckedItem = MOVE_TO_TOP_OF_CHECKED_ITEMS

        @CheckResult
        fun fromInt(value: Int): BehaviorCheckedItem {
            return values().firstOrNull { it.ordinal == value } ?: DEFAULT
        }

        @CheckResult
        fun fromString(value: String): BehaviorCheckedItem {
            return values().firstOrNull { it.name.equals(value, true) } ?: DEFAULT
        }
    }
}
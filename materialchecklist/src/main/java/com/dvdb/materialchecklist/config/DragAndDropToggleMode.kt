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

package com.dvdb.materialchecklist.config

import androidx.annotation.CheckResult
import com.dvdb.materialchecklist.config.DragAndDropToggleMode.*

/**
 * The checklist item drag-and-drop behavior.
 *
 * Using [ON_TOUCH] initiates a drag for the checklist item on-touch. This is the default behavior.
 * Using [ON_LONG_CLICK] initiates a drag for the checklist item on-click.
 * Using [NONE] disables dragging of checklist items.
 */
enum class DragAndDropToggleMode {
    ON_TOUCH,
    ON_LONG_CLICK,
    NONE;

    companion object {
        internal val defaultBehavior: DragAndDropToggleMode = ON_TOUCH

        @CheckResult
        fun fromInt(value: Int): DragAndDropToggleMode {
            return values().firstOrNull { it.ordinal == value } ?: defaultBehavior
        }

        @CheckResult
        fun fromString(value: String): DragAndDropToggleMode {
            return values().firstOrNull { it.name.equals(value, true) } ?: defaultBehavior
        }
    }
}
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
import com.dvdb.materialchecklist.config.DragAndDropDismissKeyboardBehavior.*

/**
 * The checklist item drag-and-drop behavior for when the keyboard should be dismissed.
 *
 * Using [DISMISS_KEYBOARD_ON_ITEM_DRAGGED] dismisses the keyboard when a checklist item is dragged. This is the default behavior.
 * Using [DISMISS_KEYBOARD_ON_FOCUSED_ITEM_DRAGGED] dismisses the keyboard when a checklist item that has focus is dragged.
 * Using [NONE] disables dismissing of the keyboard when checklist items are dragged.
 */
enum class DragAndDropDismissKeyboardBehavior {
    DISMISS_KEYBOARD_ON_ITEM_DRAGGED,
    DISMISS_KEYBOARD_ON_FOCUSED_ITEM_DRAGGED,
    NONE;

    companion object {
        internal val defaultBehavior: DragAndDropDismissKeyboardBehavior = DISMISS_KEYBOARD_ON_ITEM_DRAGGED

        @CheckResult
        fun fromInt(value: Int): DragAndDropDismissKeyboardBehavior {
            return values().firstOrNull { it.ordinal == value } ?: defaultBehavior
        }

        @CheckResult
        fun fromString(value: String): DragAndDropDismissKeyboardBehavior {
            return values().firstOrNull { it.name.equals(value, true) } ?: defaultBehavior
        }
    }
}
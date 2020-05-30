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

import org.junit.Assert
import org.junit.Test

class DragAndDropDismissKeyboardBehaviorTest {

    @Test
    fun fromInt_dismissKeyboardOnItemDragged_test() {
        val expected = DragAndDropDismissKeyboardBehavior.DISMISS_KEYBOARD_ON_ITEM_DRAGGED
        val actual = DragAndDropDismissKeyboardBehavior.fromInt(0)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_dismissKeyboardOnFocusedItemDragged_test() {
        val expected = DragAndDropDismissKeyboardBehavior.DISMISS_KEYBOARD_ON_FOCUSED_ITEM_DRAGGED
        val actual = DragAndDropDismissKeyboardBehavior.fromInt(1)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_none_test() {
        val expected = DragAndDropDismissKeyboardBehavior.NONE
        val actual = DragAndDropDismissKeyboardBehavior.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = DragAndDropDismissKeyboardBehavior.defaultBehavior
        val actual = DragAndDropDismissKeyboardBehavior.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_dismissKeyboardOnItemDragged_test() {
        val expected = DragAndDropDismissKeyboardBehavior.DISMISS_KEYBOARD_ON_ITEM_DRAGGED
        val actual = DragAndDropDismissKeyboardBehavior.fromString("DISMISS_KEYBOARD_ON_ITEM_DRAGGED")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_dismissKeyboardOnFocusedItemDragged_test() {
        val expected = DragAndDropDismissKeyboardBehavior.DISMISS_KEYBOARD_ON_FOCUSED_ITEM_DRAGGED
        val actual = DragAndDropDismissKeyboardBehavior.fromString("DISMISS_KEYBOARD_ON_FOCUSED_ITEM_DRAGGED")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_none_test() {
        val expected = DragAndDropDismissKeyboardBehavior.NONE
        val actual = DragAndDropDismissKeyboardBehavior.fromString("none")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = DragAndDropDismissKeyboardBehavior.defaultBehavior
        val actual = DragAndDropDismissKeyboardBehavior.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
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

package com.dvdb.materialchecklist.config.checklist.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class DragAndDropToggleBehaviorTest {

    @Test
    fun fromInt_onTouch_test() {
        val expected = DragAndDropToggleBehavior.ON_TOUCH
        val actual = DragAndDropToggleBehavior.fromInt(0)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_onLongClick_test() {
        val expected = DragAndDropToggleBehavior.ON_LONG_CLICK
        val actual = DragAndDropToggleBehavior.fromInt(1)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_none_test() {
        val expected = DragAndDropToggleBehavior.NONE
        val actual = DragAndDropToggleBehavior.fromInt(2)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = DragAndDropToggleBehavior.DEFAULT
        val actual = DragAndDropToggleBehavior.fromInt(3)

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onTouch_test() {
        val expected = DragAndDropToggleBehavior.ON_TOUCH
        val actual = DragAndDropToggleBehavior.fromString("ON_TOUCH")

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onLongClick_test() {
        val expected = DragAndDropToggleBehavior.ON_LONG_CLICK
        val actual = DragAndDropToggleBehavior.fromString("on_long_click")

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromString_none_test() {
        val expected = DragAndDropToggleBehavior.NONE
        val actual = DragAndDropToggleBehavior.fromString("NONE")

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = DragAndDropToggleBehavior.DEFAULT
        val actual = DragAndDropToggleBehavior.fromString("")

        Assertions.assertEquals(expected, actual)
    }
}
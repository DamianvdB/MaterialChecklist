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

class DragAndDropToggleModeTest {

    @Test
    fun fromInt_onTouch_test() {
        val expected = DragAndDropToggleMode.ON_TOUCH
        val actual = DragAndDropToggleMode.fromInt(0)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_onLongClick_test() {
        val expected = DragAndDropToggleMode.ON_LONG_CLICK
        val actual = DragAndDropToggleMode.fromInt(1)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_none_test() {
        val expected = DragAndDropToggleMode.NONE
        val actual = DragAndDropToggleMode.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = DragAndDropToggleMode.defaultBehavior
        val actual = DragAndDropToggleMode.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onTouch_test() {
        val expected = DragAndDropToggleMode.ON_TOUCH
        val actual = DragAndDropToggleMode.fromString("ON_TOUCH")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onLongClick_test() {
        val expected = DragAndDropToggleMode.ON_LONG_CLICK
        val actual = DragAndDropToggleMode.fromString("on_long_click")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_none_test() {
        val expected = DragAndDropToggleMode.NONE
        val actual = DragAndDropToggleMode.fromString("NONE")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = DragAndDropToggleMode.defaultBehavior
        val actual = DragAndDropToggleMode.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
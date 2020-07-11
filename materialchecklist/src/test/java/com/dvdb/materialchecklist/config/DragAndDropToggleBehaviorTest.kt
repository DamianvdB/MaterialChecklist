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

import com.dvdb.materialchecklist.config.model.DragAndDropToggleBehavior
import org.junit.Assert
import org.junit.Test

class DragAndDropToggleBehaviorTest {

    @Test
    fun fromInt_onTouch_test() {
        val expected = DragAndDropToggleBehavior.ON_TOUCH
        val actual = DragAndDropToggleBehavior.fromInt(0)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_onLongClick_test() {
        val expected = DragAndDropToggleBehavior.ON_LONG_CLICK
        val actual = DragAndDropToggleBehavior.fromInt(1)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_none_test() {
        val expected = DragAndDropToggleBehavior.NONE
        val actual = DragAndDropToggleBehavior.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = DragAndDropToggleBehavior.DEFAULT
        val actual = DragAndDropToggleBehavior.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onTouch_test() {
        val expected = DragAndDropToggleBehavior.ON_TOUCH
        val actual = DragAndDropToggleBehavior.fromString("ON_TOUCH")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_onLongClick_test() {
        val expected = DragAndDropToggleBehavior.ON_LONG_CLICK
        val actual = DragAndDropToggleBehavior.fromString("on_long_click")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_none_test() {
        val expected = DragAndDropToggleBehavior.NONE
        val actual = DragAndDropToggleBehavior.fromString("NONE")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = DragAndDropToggleBehavior.DEFAULT
        val actual = DragAndDropToggleBehavior.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
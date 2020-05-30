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

class BehaviorCheckedItemTest {

    @Test
    fun fromInt_moveToTopOfCheckedItems_test() {
        val expected = BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromInt(0)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_moveToBottomOfCheckedItems_test() {
        val expected = BehaviorCheckedItem.MOVE_TO_BOTTOM_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromInt(1)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_delete_test() {
        val expected = BehaviorCheckedItem.DELETE
        val actual = BehaviorCheckedItem.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = BehaviorCheckedItem.DEFAULT
        val actual = BehaviorCheckedItem.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_moveToTopOfCheckedItems_test() {
        val expected = BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromString("MOVE_TO_TOP_OF_CHECKED_ITEMS")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_moveToBottomOfCheckedItems_test() {
        val expected = BehaviorCheckedItem.MOVE_TO_BOTTOM_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromString("MOVE_TO_BOTTOM_OF_CHECKED_ITEMS")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_delete_test() {
        val expected = BehaviorCheckedItem.DELETE
        val actual = BehaviorCheckedItem.fromString("delete")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = BehaviorCheckedItem.DEFAULT
        val actual = BehaviorCheckedItem.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
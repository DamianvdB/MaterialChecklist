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
    fun fromInt_keepPosition_test() {
        val expected = BehaviorCheckedItem.KEEP_POSITION
        val actual = BehaviorCheckedItem.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_delete_test() {
        val expected = BehaviorCheckedItem.DELETE
        val actual = BehaviorCheckedItem.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromInt(4)

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
    fun fromString_keepPosition_test() {
        val expected = BehaviorCheckedItem.KEEP_POSITION
        val actual = BehaviorCheckedItem.fromString("keep_position")

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
        val expected = BehaviorCheckedItem.MOVE_TO_TOP_OF_CHECKED_ITEMS
        val actual = BehaviorCheckedItem.fromString("")

        Assert.assertEquals(expected, actual)
    }


}
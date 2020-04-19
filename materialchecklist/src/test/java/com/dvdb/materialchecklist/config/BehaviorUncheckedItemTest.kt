package com.dvdb.materialchecklist.config

import org.junit.Assert
import org.junit.Test

class BehaviorUncheckedItemTest {

    @Test
    fun fromInt_moveToPreviousPosition_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION
        val actual = BehaviorUncheckedItem.fromInt(0)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_moveToBottomOfUncheckedItems_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS
        val actual = BehaviorUncheckedItem.fromInt(1)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_moveToTopOfUncheckedItems_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_TOP_OF_UNCHECKED_ITEMS
        val actual = BehaviorUncheckedItem.fromInt(2)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromInt_invalid_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION
        val actual = BehaviorUncheckedItem.fromInt(3)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_moveToPreviousPosition_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION
        val actual = BehaviorUncheckedItem.fromString("MOVE_TO_PREVIOUS_POSITION")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_moveToBottomOfUncheckedItems_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS
        val actual = BehaviorUncheckedItem.fromString("MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_moveToTopOfUncheckedItems_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_TOP_OF_UNCHECKED_ITEMS
        val actual = BehaviorUncheckedItem.fromString("move_to_top_of_unchecked_items")

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun fromString_invalid_test() {
        val expected = BehaviorUncheckedItem.MOVE_TO_PREVIOUS_POSITION
        val actual = BehaviorUncheckedItem.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
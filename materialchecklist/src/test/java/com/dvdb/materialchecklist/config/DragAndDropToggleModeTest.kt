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
        val expected = DragAndDropToggleMode.ON_TOUCH
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
        val expected = DragAndDropToggleMode.ON_TOUCH
        val actual = DragAndDropToggleMode.fromString("")

        Assert.assertEquals(expected, actual)
    }
}
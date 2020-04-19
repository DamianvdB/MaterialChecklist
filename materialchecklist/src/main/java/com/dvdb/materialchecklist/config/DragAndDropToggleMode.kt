package com.dvdb.materialchecklist.config

enum class DragAndDropToggleMode {
    ON_TOUCH,
    ON_LONG_CLICK,
    NONE;

    companion object {
        fun fromInt(value: Int): DragAndDropToggleMode {
            return values().firstOrNull { it.ordinal == value } ?: ON_TOUCH
        }

        fun fromString(value: String): DragAndDropToggleMode {
            return values().firstOrNull { it.name.equals(value, true) } ?: ON_TOUCH
        }
    }
}
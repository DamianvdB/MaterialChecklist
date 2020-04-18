package com.dvdb.materialchecklist.config

enum class BehaviorCheckedItem {
    MOVE_TO_TOP_OF_CHECKED_ITEMS,
    MOVE_TO_BOTTOM_OF_CHECKED_ITEMS,
    KEEP_POSITION,
    DELETE;

    companion object {
        fun fromInt(value: Int): BehaviorCheckedItem {
            return values().firstOrNull { it.ordinal == value } ?: MOVE_TO_TOP_OF_CHECKED_ITEMS
        }

        fun fromString(value: String): BehaviorCheckedItem {
            return values().firstOrNull { it.name.equals(value, true) } ?: MOVE_TO_TOP_OF_CHECKED_ITEMS
        }
    }
}
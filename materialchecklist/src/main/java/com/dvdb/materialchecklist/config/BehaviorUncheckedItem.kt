package com.dvdb.materialchecklist.config

enum class BehaviorUncheckedItem {
    MOVE_TO_PREVIOUS_POSITION,
    MOVE_TO_BOTTOM_OF_UNCHECKED_ITEMS,
    MOVE_TO_TOP_OF_UNCHECKED_ITEMS;

    companion object {
        fun fromInt(value: Int): BehaviorUncheckedItem {
            return values().firstOrNull { it.ordinal == value } ?: MOVE_TO_PREVIOUS_POSITION
        }

        fun fromString(value: String): BehaviorUncheckedItem {
            return values().firstOrNull { it.name.equals(value, true) } ?: MOVE_TO_PREVIOUS_POSITION
        }
    }
}
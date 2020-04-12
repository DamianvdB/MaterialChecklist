package com.dvdb.checklist.util

internal fun String.substringOrNull(
    startIndex: Int,
    endIndex: Int
): String? {
    return try {
        substring(startIndex, endIndex)
    } catch (e: IndexOutOfBoundsException) {
        null
    }
}
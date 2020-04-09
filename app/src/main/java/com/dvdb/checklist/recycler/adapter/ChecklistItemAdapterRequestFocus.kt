package com.dvdb.checklist.recycler.adapter

internal data class ChecklistItemAdapterRequestFocus(
    val position: Int,
    val isStartSelection: Boolean = false,
    val isShowKeyboard: Boolean = false
)
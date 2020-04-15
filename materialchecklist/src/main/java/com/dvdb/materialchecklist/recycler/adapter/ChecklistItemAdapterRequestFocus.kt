package com.dvdb.materialchecklist.recycler.adapter

internal data class ChecklistItemAdapterRequestFocus(
    val position: Int,
    val isStartSelection: Boolean = false,
    val isShowKeyboard: Boolean = false
)
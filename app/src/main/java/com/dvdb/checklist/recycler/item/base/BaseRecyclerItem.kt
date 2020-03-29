package com.dvdb.checklist.recycler.item.base

internal abstract class BaseRecyclerItem {
    abstract val type: Type

    enum class Type {
        CHECKLIST,
        CHECKLIST_NEW,
        CHECKLIST_DIVIDER
    }
}
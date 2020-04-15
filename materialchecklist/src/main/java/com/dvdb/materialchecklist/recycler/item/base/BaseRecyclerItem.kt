package com.dvdb.materialchecklist.recycler.item.base

internal abstract class BaseRecyclerItem {
    abstract val type: Type

    enum class Type {
        CHECKLIST,
        CHECKLIST_NEW,
    }
}
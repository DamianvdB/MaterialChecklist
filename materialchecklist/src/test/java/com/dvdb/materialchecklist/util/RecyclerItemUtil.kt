package com.dvdb.materialchecklist.util

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem

internal fun List<BaseRecyclerItem>.resetIds(): List<BaseRecyclerItem> {
    return map { item ->
        if (item is ChecklistRecyclerItem) {
            item.copy(id = "")
        } else {
            item
        }
    }
}
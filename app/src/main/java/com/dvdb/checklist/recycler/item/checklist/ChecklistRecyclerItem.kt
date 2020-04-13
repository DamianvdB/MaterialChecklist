package com.dvdb.checklist.recycler.item.checklist

import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem

internal data class ChecklistRecyclerItem(
    val text: String,
    val isChecked: Boolean = false,
    override val type: Type = Type.CHECKLIST,
    val id: String = java.util.UUID.randomUUID().toString()
) : BaseRecyclerItem()

package com.dvdb.materialchecklist.recycler.item.checklist

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem

internal data class ChecklistRecyclerItem(
    val text: String,
    val isChecked: Boolean = false,
    override val type: Type = Type.CHECKLIST,
    val id: String = java.util.UUID.randomUUID().toString()
) : BaseRecyclerItem()

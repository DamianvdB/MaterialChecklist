package com.dvdb.checklist.recycler.item.checklist

import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem

internal data class ChecklistRecyclerItem(
    val content: String,
    val isChecked: Boolean = false,
    val isRequestFocus: Boolean = false,
    override val type: Type = Type.CHECKLIST
) : BaseRecyclerItem()

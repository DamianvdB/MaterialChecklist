package com.dvdb.checklist.recycler.item.checklistnew

import com.dvdb.checklist.recycler.item.base.BaseRecyclerItem

internal data class ChecklistNewRecyclerItem(
    val content: String,
    override val type: Type = Type.CHECKLIST_NEW
) : BaseRecyclerItem()
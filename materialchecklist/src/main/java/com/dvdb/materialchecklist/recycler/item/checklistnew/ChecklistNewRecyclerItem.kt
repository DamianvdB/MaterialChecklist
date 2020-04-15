package com.dvdb.materialchecklist.recycler.item.checklistnew

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem

internal data class ChecklistNewRecyclerItem(
    override val type: Type = Type.CHECKLIST_NEW
) : BaseRecyclerItem()
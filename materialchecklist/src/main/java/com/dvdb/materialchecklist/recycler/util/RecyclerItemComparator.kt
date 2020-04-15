package com.dvdb.materialchecklist.recycler.util

import com.dvdb.materialchecklist.recycler.item.base.BaseRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.item.checklistnew.ChecklistNewRecyclerItem

internal sealed class BaseRecyclerItemComparator : Comparator<BaseRecyclerItem> {

    override fun compare(item1: BaseRecyclerItem?, item2: BaseRecyclerItem?): Int {
        return when {
            item1 is ChecklistRecyclerItem && item2 is ChecklistRecyclerItem -> compareIf(item1, item2)
            item1 is ChecklistRecyclerItem && item2 is ChecklistNewRecyclerItem -> compareIf(item1, item2)
            item1 is ChecklistNewRecyclerItem && item2 is ChecklistRecyclerItem -> compareIf(item1, item2)
            else -> 0
        }
    }

    abstract fun compareIf(item1: ChecklistRecyclerItem, item2: ChecklistRecyclerItem): Int
    abstract fun compareIf(item1: ChecklistRecyclerItem, item2: ChecklistNewRecyclerItem): Int
    abstract fun compareIf(item1: ChecklistNewRecyclerItem, item2: ChecklistRecyclerItem): Int
}

internal object DefaultRecyclerItemComparator : BaseRecyclerItemComparator() {

    override fun compareIf(item1: ChecklistRecyclerItem, item2: ChecklistRecyclerItem) = item1.isChecked.compareTo(item2.isChecked)

    override fun compareIf(item1: ChecklistRecyclerItem, item2: ChecklistNewRecyclerItem) = if (item1.isChecked) 1 else -1

    override fun compareIf(item1: ChecklistNewRecyclerItem, item2: ChecklistRecyclerItem) = if (item2.isChecked) -1 else 1
}

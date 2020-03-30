package com.dvdb.checklist.recycler.adapter.config

import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig

internal data class ChecklistItemAdapterConfig(
    val checklistConfig: ChecklistRecyclerHolderConfig,
    val checklistNewConfig: ChecklistNewRecyclerHolderConfig
)
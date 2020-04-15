package com.dvdb.materialchecklist.recycler.adapter.config

import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig

internal data class ChecklistItemAdapterConfig(
    val checklistConfig: ChecklistRecyclerHolderConfig,
    val checklistNewConfig: ChecklistNewRecyclerHolderConfig
) : Config
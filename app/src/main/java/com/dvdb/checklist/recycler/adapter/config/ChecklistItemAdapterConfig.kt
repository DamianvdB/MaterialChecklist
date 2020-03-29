package com.dvdb.checklist.recycler.adapter.config

import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderThemeConfig
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderThemeConfig

internal data class ChecklistItemAdapterConfig(
    val checklistConfig: ChecklistRecyclerHolderThemeConfig,
    val checklistNewConfig: ChecklistNewRecyclerHolderThemeConfig
)
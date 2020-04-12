package com.dvdb.checklist.manager.config

import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig

internal data class ChecklistManagerConfig(
    val dragAndDropEnabled: Boolean,
    val adapterConfig: ChecklistItemAdapterConfig
)
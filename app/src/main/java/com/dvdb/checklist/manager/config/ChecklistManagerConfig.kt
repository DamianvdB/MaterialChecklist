package com.dvdb.checklist.manager.config

import com.dvdb.checklist.config.CheckedItemBehavior
import com.dvdb.checklist.config.Config
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig

internal data class ChecklistManagerConfig(
    val dragAndDropEnabled: Boolean,
    val behaviorCheckedItem: CheckedItemBehavior,
    val adapterConfig: ChecklistItemAdapterConfig
): Config
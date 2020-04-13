package com.dvdb.checklist.manager.config

import com.dvdb.checklist.config.BehaviorCheckedItem
import com.dvdb.checklist.config.BehaviorUncheckedItem
import com.dvdb.checklist.config.Config
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig

internal data class ChecklistManagerConfig(
    val dragAndDropEnabled: Boolean,
    val behaviorCheckedItem: BehaviorCheckedItem,
    val behaviorUncheckedItem: BehaviorUncheckedItem,
    val adapterConfig: ChecklistItemAdapterConfig
) : Config
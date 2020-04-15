package com.dvdb.materialchecklist.manager.config

import com.dvdb.materialchecklist.config.BehaviorCheckedItem
import com.dvdb.materialchecklist.config.BehaviorUncheckedItem
import com.dvdb.materialchecklist.config.Config
import com.dvdb.materialchecklist.recycler.adapter.config.ChecklistItemAdapterConfig

internal data class ChecklistManagerConfig(
    val dragAndDropEnabled: Boolean,
    val behaviorCheckedItem: BehaviorCheckedItem,
    val behaviorUncheckedItem: BehaviorUncheckedItem,
    val adapterConfig: ChecklistItemAdapterConfig
) : Config
package com.dvdb.checklist.recycler.adapter

internal interface ChecklistItemAdapterDragListener {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun canDragOverTargetItem(currentPosition: Int, targetPosition: Int): Boolean

    fun onItemDragStart() {}

    fun onItemDragStop() {}
}
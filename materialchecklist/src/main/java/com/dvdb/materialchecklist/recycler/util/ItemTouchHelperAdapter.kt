package com.dvdb.materialchecklist.recycler.util

import androidx.recyclerview.widget.RecyclerView

internal interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean

    fun canDragOverTarget(current: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean

    fun onDragStart(viewHolder: RecyclerView.ViewHolder)

    fun onDragStop(viewHolder: RecyclerView.ViewHolder)
}
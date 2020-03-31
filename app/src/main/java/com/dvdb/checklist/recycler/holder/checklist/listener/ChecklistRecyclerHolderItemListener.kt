package com.dvdb.checklist.recycler.holder.checklist.listener

internal interface ChecklistRecyclerHolderItemListener {
    fun onItemChecked(position: Int, isChecked: Boolean)

    fun onItemTextChanged(position: Int, text: String)

    fun onItemEnterKeyPressed(position: Int)

    fun onItemDeleteClicked(position: Int)
}
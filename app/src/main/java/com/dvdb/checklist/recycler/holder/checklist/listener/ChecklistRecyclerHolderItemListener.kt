package com.dvdb.checklist.recycler.holder.checklist.listener

import android.widget.TextView

internal interface ChecklistRecyclerHolderItemListener {

    fun onItemChecked(position: Int, isChecked: Boolean)

    fun onItemTextChanged(position: Int, text: String)

    fun onItemEnterKeyPressed(position: Int, textView: TextView)

    fun onItemDeleteClicked(position: Int)

    fun onItemFocusChanged(position: Int, hasFocus: Boolean)

    fun onItemDragHandledClicked(position: Int)
}
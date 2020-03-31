package com.dvdb.checklist.recycler.holder.checklistnew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dvdb.checklist.R
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.checklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.checklist.util.setTintCompat
import kotlinx.android.synthetic.main.item_checklist_new.view.*

internal class ChecklistNewRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistNewRecyclerHolderConfig,
    private val onItemClicked: (position: Int) -> Unit
) : BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig>(itemView, config) {

    init {
        initialiseView()
    }

    private fun initialiseView() {
        initialiseRoot()
        initialiseAdd()
        initialiseText()
    }

    private fun initialiseRoot() {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    private fun initialiseAdd() {
        itemView.item_checklist_new_add.drawable.setTintCompat(config.addTintColor)
    }

    private fun initialiseText() {
        itemView.item_checklist_new_text.setTextColor(config.textColor)

        config.textSizeSP?.let {
            itemView.item_checklist_new_text.textSize = it
        }

        config.typeFace?.let {
            itemView.item_checklist_new_text.typeface = it
        }
    }

    override fun bindView(item: ChecklistNewRecyclerItem) {
        itemView.item_checklist_new_text.text = item.text
    }

    override fun onConfigUpdated() {
        initialiseView()
    }

    internal class Factory(
        private val onItemClicked: (position: Int) -> Unit
    ) : BaseRecyclerHolderFactory<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChecklistNewRecyclerHolderConfig
        ): BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderConfig> {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_checklist_new,
                parent,
                false
            )
            return ChecklistNewRecyclerHolder(
                itemView,
                config,
                onItemClicked
            )
        }
    }
}
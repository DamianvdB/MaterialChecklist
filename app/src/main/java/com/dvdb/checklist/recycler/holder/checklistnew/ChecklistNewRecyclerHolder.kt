package com.dvdb.checklist.recycler.holder.checklistnew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dvdb.checklist.R
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.checklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderThemeConfig
import com.dvdb.checklist.recycler.item.checklistnew.ChecklistNewRecyclerItem
import com.dvdb.checklist.util.setTintCompat
import kotlinx.android.synthetic.main.item_checklist_new.view.*

internal class ChecklistNewRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistNewRecyclerHolderThemeConfig,
    private val onItemClicked: (position: Int) -> Unit
) : BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig>(itemView, config) {

    override fun initialiseView() {
        initialiseRoot()
        initialiseAdd()
        initialiseContent()
    }

    private fun initialiseRoot() {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    private fun initialiseAdd() {
        itemView.item_checklist_new_add.drawable.setTintCompat(config.addTintColor)
    }

    private fun initialiseContent() {
        itemView.item_checklist_new_content.setTextColor(config.contentTextColor)

        itemView.item_checklist_new_content.textSize = config.contentTextSizeSP

        config.contentTypeFace?.let {
            itemView.item_checklist_new_content.typeface = it
        }
    }

    override fun bindView(item: ChecklistNewRecyclerItem) {
        itemView.item_checklist_new_content.text = item.content
    }

    internal class Factory(
        private val onItemClicked: (position: Int) -> Unit
    ) : BaseRecyclerHolderFactory<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChecklistNewRecyclerHolderThemeConfig
        ): BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig> {
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
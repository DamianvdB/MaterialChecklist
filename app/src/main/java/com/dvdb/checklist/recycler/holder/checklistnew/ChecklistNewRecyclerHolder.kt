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

private class ChecklistNewRecyclerHolder(
    itemView: View,
    config: ChecklistNewRecyclerHolderThemeConfig,
    private val onItemClicked: (position: Int) -> Unit
) : BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig>(itemView) {

    init {
        initialiseView(config)
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }

    private fun initialiseView(config: ChecklistNewRecyclerHolderThemeConfig) {
        initialiseAdd(config)
        initialiseContent(config)
    }

    private fun initialiseAdd(config: ChecklistNewRecyclerHolderThemeConfig) {
        itemView.item_checklist_new_add.drawable.setTintCompat(config.addTintColor)
    }

    private fun initialiseContent(config: ChecklistNewRecyclerHolderThemeConfig) {
        itemView.item_checklist_new_content.setTextColor(config.contentTextColor)

        itemView.item_checklist_new_content.textSize = config.contentTextSizeSP

        config.contentTypeFace?.let {
            itemView.item_checklist_new_content.typeface = it
        }
    }

    override fun updateThemeConfig(config: ChecklistNewRecyclerHolderThemeConfig) {
        initialiseView(config)
    }

    override fun bindView(item: ChecklistNewRecyclerItem) {
        itemView.item_checklist_new_content.text = item.content
    }
}

internal class ChecklistNewRecyclerHolderFactory(
    private val parent: ViewGroup,
    private val config: ChecklistNewRecyclerHolderThemeConfig,
    private val onItemClicked: (position: Int) -> Unit
) : BaseRecyclerHolderFactory<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig> {

    override fun create(): BaseRecyclerHolder<ChecklistNewRecyclerItem, ChecklistNewRecyclerHolderThemeConfig> {
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
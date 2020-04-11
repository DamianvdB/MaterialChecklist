package com.dvdb.checklist.recycler.holder.checklistnew

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    private val addIcon: ImageView = itemView.item_checklist_new_add
    private val text: TextView = itemView.item_checklist_new_text

    init {
        initialiseView()
    }

    override fun bindView(item: ChecklistNewRecyclerItem) {
    }

    override fun onConfigUpdated() {
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
        addIcon.drawable.setTintCompat(config.iconTintColor)
        addIcon.alpha = config.iconAlphaAdd
    }

    private fun initialiseText() {
        text.text = config.text
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.textSize)
        text.setTextColor(config.textColor)
        text.alpha = config.textAlpha

        config.typeFace?.let {
            text.typeface = it
        }
    }

    class Factory(
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
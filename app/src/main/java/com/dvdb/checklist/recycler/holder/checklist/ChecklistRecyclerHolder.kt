package com.dvdb.checklist.recycler.holder.checklist

import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dvdb.checklist.R
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.checklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.checklist.recycler.holder.util.EnterKeyListenerFactory
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.util.SimpleTextChangedListener
import com.dvdb.checklist.util.setTintCompat
import com.dvdb.checklist.util.setVisible
import kotlinx.android.synthetic.main.item_checklist.view.*

internal class ChecklistRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistRecyclerHolderConfig,
    private val enterKeyListenerFactory: EnterKeyListenerFactory,
    private val listener: ChecklistRecyclerHolderItemListener
) : BaseRecyclerHolder<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig>(itemView, config) {

    override fun initialiseView() {
        initialiseDragIndicator()
        initialiseCheckbox()
        initialiseContent()
        initialiseDelete()
    }

    private fun initialiseDragIndicator() {
        itemView.item_checklist_drag_indicator.drawable.setTintCompat(config.dragIndicatorTintColor)
    }

    private fun initialiseCheckbox() {
        itemView.item_checklist_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                itemView.item_checklist_content.paintFlags =
                    itemView.item_checklist_content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemView.item_checklist_content.setTextColor(config.contentCheckedTextColor)
            } else {
                itemView.item_checklist_content.paintFlags =
                    itemView.item_checklist_content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                itemView.item_checklist_content.setTextColor(config.contentUncheckedTextColor)
            }

            listener.onItemChecked(adapterPosition, isChecked)
        }
    }

    private fun initialiseContent() {
        itemView.item_checklist_content.textSize = config.contentTextSizeSP

        config.contentTypeFace?.let {
            itemView.item_checklist_content.typeface = it
        }

        itemView.item_checklist_content.addTextChangedListener(
            object : SimpleTextChangedListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (itemView.item_checklist_content.isFocused) {
                        listener.onItemContentChanged(adapterPosition, s.toString())
                    }
                }
            })

        itemView.item_checklist_content.setOnFocusChangeListener { _, hasFocus ->
            itemView.item_checklist_delete.setVisible(
                hasFocus,
                View.INVISIBLE
            )
        }

        itemView.item_checklist_content.setOnKeyListener(enterKeyListenerFactory.create {
            listener.onItemEnterKeyPressed(adapterPosition)
        })
    }

    private fun initialiseDelete() {
        itemView.item_checklist_delete.drawable.setTintCompat(config.deleteTintColor)

        itemView.item_checklist_delete.setOnClickListener {
//            itemView.item_checklist_content.requestFocus()
            listener.onItemDeleteClicked(adapterPosition)
        }
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        itemView.item_checklist_checkbox.isChecked = item.isChecked

        itemView.item_checklist_content.setText(item.content)
        if (item.isRequestFocus) {
            itemView.item_checklist_content.requestFocus()
        }
    }

    class Factory(
        private val enterKeyListenerFactory: EnterKeyListenerFactory,
        private val listener: ChecklistRecyclerHolderItemListener
    ) : BaseRecyclerHolderFactory<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ChecklistRecyclerHolderConfig
        ): BaseRecyclerHolder<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig> {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_checklist,
                parent,
                false
            )
            return ChecklistRecyclerHolder(
                itemView,
                config,
                enterKeyListenerFactory,
                listener
            )
        }
    }
}
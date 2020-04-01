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

    init {
        initialiseView()
    }

    private fun initialiseView() {
        initialiseDragIndicator()
        initialiseCheckbox()
        initialiseText()
        initialiseDelete()
    }

    private fun initialiseDragIndicator() {
        itemView.item_checklist_drag_indicator.drawable.setTintCompat(config.dragIndicatorTintColor)
    }

    private fun initialiseCheckbox() {
        itemView.item_checklist_checkbox.setOnCheckedChangeListener { _, isChecked ->
            updateTextAppearanceForCheckedState(isChecked)
            updateDragIndicatorVisibility(isChecked)
            listener.onItemChecked(adapterPosition, isChecked)
        }
    }

    private fun initialiseText() {
        config.textSizeSP?.let {
            itemView.item_checklist_text.textSize = it
        }

        config.typeFace?.let {
            itemView.item_checklist_text.typeface = it
        }

        itemView.item_checklist_text.addTextChangedListener(
            object : SimpleTextChangedListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (itemView.item_checklist_text.isFocused) {
                        listener.onItemTextChanged(adapterPosition, s.toString())
                    }
                }
            })

        itemView.item_checklist_text.setOnFocusChangeListener { _, hasFocus ->
            itemView.item_checklist_delete.setVisible(
                hasFocus,
                View.INVISIBLE
            )
        }

        itemView.item_checklist_text.setOnKeyListener(enterKeyListenerFactory.create {
            listener.onItemEnterKeyPressed(adapterPosition)
        })
    }

    private fun initialiseDelete() {
        itemView.item_checklist_delete.drawable.setTintCompat(config.deleteTintColor)

        itemView.item_checklist_delete.setOnClickListener {
//            itemView.item_checklist_text.requestFocus()
            listener.onItemDeleteClicked(adapterPosition)
        }
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        if (itemView.item_checklist_checkbox.isChecked == item.isChecked) {
            updateDragIndicatorVisibility(item.isChecked)
            updateTextAppearanceForCheckedState(item.isChecked)
        }

        itemView.item_checklist_checkbox.isChecked = item.isChecked

        itemView.item_checklist_text.setText(item.text)

        if (item.isRequestFocus) {
            itemView.item_checklist_text.requestFocus()
        }
    }

    private fun updateDragIndicatorVisibility(isChecked: Boolean) {
        itemView.item_checklist_drag_indicator.setVisible(!isChecked, View.INVISIBLE)
    }

    private fun updateTextAppearanceForCheckedState(isChecked: Boolean) {
        if (isChecked) {
            itemView.item_checklist_text.paintFlags =
                itemView.item_checklist_text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            itemView.item_checklist_text.setTextColor(config.checkedTextColor)
        } else {
            itemView.item_checklist_text.paintFlags =
                itemView.item_checklist_text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            itemView.item_checklist_text.setTextColor(config.uncheckedTextColor)
        }
    }

    override fun onConfigUpdated() {
        initialiseView()
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
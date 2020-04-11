package com.dvdb.checklist.recycler.holder.checklist

import android.graphics.Paint
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.R
import com.dvdb.checklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.checklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.checklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.checklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.checklist.util.SimpleTextChangedListener
import com.dvdb.checklist.util.setTintCompat
import com.dvdb.checklist.util.setVisible
import com.dvdb.checklist.util.showKeyboard
import com.dvdb.checklist.widget.CheckboxWidget
import com.dvdb.checklist.widget.EditTextWidget
import kotlinx.android.synthetic.main.item_checklist.view.*

internal class ChecklistRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistRecyclerHolderConfig,
    private val enterActionActionFactory: EnterActionPerformedFactory,
    private val listener: ChecklistRecyclerHolderItemListener
) : BaseRecyclerHolder<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig>(itemView, config) {

    private val dragIndicatorIcon: ImageView = itemView.item_checklist_drag_indicator
    private val checkbox: CheckboxWidget = itemView.item_checklist_checkbox
    private val text: EditTextWidget = itemView.item_checklist_text
    private val deleteIcon: ImageView = itemView.item_checklist_delete

    init {
        initialiseView()
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        text.setText(item.text)
        checkbox.setChecked(item.isChecked, false)

        updateDragIndicatorVisibility(item.isChecked)
        updateTextAppearanceForCheckedState(item.isChecked)
    }

    override fun onConfigUpdated() {
        initialiseView()
    }

    fun requestFocus(isStartSelection: Boolean, isShowKeyboard: Boolean) {
        text.requestFocus()
        text.setSelection(if (isStartSelection) 0 else text.length())

        if (isShowKeyboard) {
            text.showKeyboard()
        }
    }

    private fun initialiseView() {
        initialiseDragIndicator()
        initialiseCheckbox()
        initialiseText()
        initialiseDelete()
    }

    private fun initialiseDragIndicator() {
        dragIndicatorIcon.drawable.setTintCompat(config.dragIndicatorTintColor)
    }

    private fun initialiseCheckbox() {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            listener.onItemChecked(adapterPosition, isChecked)
        }
    }

    private fun initialiseText() {
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.textSize)

        config.typeFace?.let {
            text.typeface = it
        }

        text.addTextChangedListener(
            object : SimpleTextChangedListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (text.isFocused && adapterPosition != RecyclerView.NO_POSITION) {
                        s?.let { text ->
                            listener.onItemTextChanged(adapterPosition, text.toString())
                        }
                    }
                }
            })

        text.setOnFocusChangeListener { _, hasFocus ->
            deleteIcon.setVisible(
                hasFocus,
                View.INVISIBLE
            )
            listener.onItemFocusChanged(adapterPosition, hasFocus)
        }

        text.setOnEditorActionListener(
            enterActionActionFactory.create(
                runnable = {
                    listener.onItemEnterKeyPressed(adapterPosition, text)
                    true
                },
                preConditions = {
                    !text.isFocused
                }
            )
        )
    }

    private fun initialiseDelete() {
        deleteIcon.drawable.setTintCompat(config.deleteTintColor)

        deleteIcon.setOnClickListener {
            listener.onItemDeleteClicked(adapterPosition)
        }
    }

    private fun updateDragIndicatorVisibility(isChecked: Boolean) {
        dragIndicatorIcon.setVisible(!isChecked, View.INVISIBLE)
    }

    private fun updateTextAppearanceForCheckedState(isChecked: Boolean) {
        if (isChecked) {
            text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text.setTextColor(config.checkedTextColor)
        } else {
            text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            text.setTextColor(config.uncheckedTextColor)
        }
    }

    class Factory(
        private val enterActionPerformedFactory: EnterActionPerformedFactory,
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
                enterActionPerformedFactory,
                listener
            )
        }
    }
}
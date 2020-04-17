package com.dvdb.materialchecklist.recycler.holder.checklist

import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.CompoundButtonCompat
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.holder.DraggableRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.base.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.holder.base.factory.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.holder.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.materialchecklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.materialchecklist.recycler.item.checklist.ChecklistRecyclerItem
import com.dvdb.materialchecklist.util.SimpleTextChangedListener
import com.dvdb.materialchecklist.util.setTintCompat
import com.dvdb.materialchecklist.util.setVisible
import com.dvdb.materialchecklist.util.showKeyboard
import com.dvdb.materialchecklist.widget.CheckboxWidget
import com.dvdb.materialchecklist.widget.EditTextWidget
import kotlinx.android.synthetic.main.item_checklist.view.*

private const val DEFAULT_ALPHA = 1.0f

internal class ChecklistRecyclerHolder private constructor(
    itemView: View,
    config: ChecklistRecyclerHolderConfig,
    private val enterActionActionFactory: EnterActionPerformedFactory,
    private val listener: ChecklistRecyclerHolderItemListener
) : BaseRecyclerHolder<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig>(itemView, config),
    DraggableRecyclerHolder {

    private val defaultBackground: Drawable? = itemView.background
    private val defaultElevation: Float? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) itemView.elevation else null

    private val dragIndicatorIcon: ImageView = itemView.item_checklist_drag_indicator
    private val checkbox: CheckboxWidget = itemView.item_checklist_checkbox
    private val text: EditTextWidget = itemView.item_checklist_text
    private val deleteIcon: ImageView = itemView.item_checklist_delete

    init {
        initialiseView()
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        dragIndicatorIcon.setVisible(!item.isChecked && config.iconVisibleDragIndicator, View.INVISIBLE)

        checkbox.setChecked(item.isChecked, false)
        checkbox.alpha = if (item.isChecked) config.checkboxAlphaCheckedItem else DEFAULT_ALPHA

        text.setText(item.text)
        updateTextAppearanceForCheckedState(item.isChecked)
    }

    override fun onConfigUpdated() {
        initialiseView()
    }

    override fun onDragStart() {
        config.dragActiveBackgroundColor?.let {
            itemView.setBackgroundColor(it)
        }

        config.dragActiveElevation?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.elevation = it
            }
        }
    }

    override fun onDragStop() {
        if (config.dragActiveBackgroundColor != null) {
            itemView.background = defaultBackground
        }

        if (config.dragActiveElevation != null && defaultElevation != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                itemView.elevation = defaultElevation
            }
        }
    }

    fun requestFocus(isStartSelection: Boolean, isShowKeyboard: Boolean) {
        text.requestFocus()
        text.setSelection(if (isStartSelection) 0 else text.length())

        if (isShowKeyboard) {
            text.showKeyboard()
        }
    }

    private fun initialiseView() {
        initialiseRoot()
        initialiseDragIndicator()
        initialiseCheckbox()
        initialiseText()
        initialiseDelete()
    }

    private fun initialiseRoot() {
        config.horizontalPadding?.toInt()?.let { padding ->
            itemView.setPadding(
                padding,
                itemView.paddingTop,
                padding,
                itemView.paddingBottom
            )
        }
    }

    private fun initialiseDragIndicator() {
        dragIndicatorIcon.drawable.setTintCompat(config.iconTintColor)
        dragIndicatorIcon.alpha = config.iconAlphaDragIndicator
        dragIndicatorIcon.setVisible(config.iconVisibleDragIndicator, View.INVISIBLE)

        dragIndicatorIcon.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                listener.onItemDragHandledClicked(adapterPosition)
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    private fun initialiseCheckbox() {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            listener.onItemChecked(adapterPosition, isChecked)
        }

        config.checkboxTintColor?.let {
            CompoundButtonCompat.setButtonTintList(checkbox, ColorStateList.valueOf(it))
        }
    }

    private fun initialiseText() {
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.textSize)

        config.textTypeFace?.let {
            text.typeface = it
        }

        text.addTextChangedListener(
            object : SimpleTextChangedListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (text.isFocused) {
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

        text.onDeleteKeyPressed = {
            listener.onItemDeleteKeyPressed(adapterPosition)
        }
    }

    private fun initialiseDelete() {
        deleteIcon.drawable.setTintCompat(config.iconTintColor)
        deleteIcon.alpha = config.iconAlphaDelete

        deleteIcon.setOnClickListener {
            listener.onItemDeleteClicked(adapterPosition)
        }
    }

    private fun updateTextAppearanceForCheckedState(isChecked: Boolean) {
        if (isChecked) {
            text.paintFlags = text.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text.alpha = config.textAlphaCheckedItem
        } else {
            text.paintFlags = text.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            text.alpha = DEFAULT_ALPHA
        }

        text.setTextColor(config.textColor)
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
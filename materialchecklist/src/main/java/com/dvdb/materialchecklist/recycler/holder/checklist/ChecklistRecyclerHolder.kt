/*
 * Designed and developed by Damian van den Berg.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dvdb.materialchecklist.recycler.holder.checklist

import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Editable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.CompoundButtonCompat
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.config.DragAndDropToggleMode
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

    private val dragIndicatorIcon: ImageView = itemView.item_checklist_drag_indicator
    private val checkbox: CheckboxWidget = itemView.item_checklist_checkbox
    private val text: EditTextWidget = itemView.item_checklist_text
    private val deleteIcon: ImageView = itemView.item_checklist_delete

    init {
        initialiseView()
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        dragIndicatorIcon.setVisible(
            !item.isChecked && config.dragAndDropToggleMode != DragAndDropToggleMode.NONE,
            View.INVISIBLE
        )

        checkbox.setChecked(item.isChecked, false)
        checkbox.alpha = if (item.isChecked) config.checkboxAlphaCheckedItem else DEFAULT_ALPHA

        text.setText(item.text)
        updateTextAppearanceForCheckedState(item.isChecked)
    }

    override fun onConfigUpdated() {
        initialiseView()
    }

    override fun onDragStart() {
        config.dragAndDropActiveBackgroundColor?.let {
            itemView.setBackgroundColor(it)
        }
    }

    override fun onDragStop() {
        if (config.dragAndDropActiveBackgroundColor != null) {
            itemView.background = defaultBackground
        }
    }

    fun requestFocus(selectionPosition: Int, isShowKeyboard: Boolean) {
        text.requestFocus()
        text.setSelection(selectionPosition.coerceIn(0, text.length()))

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
        dragIndicatorIcon.setVisible(
            config.dragAndDropToggleMode != DragAndDropToggleMode.NONE,
            View.INVISIBLE
        )

        when (config.dragAndDropToggleMode) {
            DragAndDropToggleMode.ON_TOUCH -> {
                dragIndicatorIcon.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        listener.onItemDragHandledClicked(adapterPosition)
                        return@setOnTouchListener true
                    }
                    return@setOnTouchListener false
                }

                dragIndicatorIcon.setOnLongClickListener(null)
            }

            DragAndDropToggleMode.ON_LONG_CLICK -> {
                dragIndicatorIcon.setOnLongClickListener {
                    listener.onItemDragHandledClicked(adapterPosition)
                    return@setOnLongClickListener true
                }

                dragIndicatorIcon.setOnTouchListener { _, _ -> false }
            }

            DragAndDropToggleMode.NONE -> {
            }
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

            listener.onItemFocusChanged(
                position = adapterPosition,
                startSelection = text.selectionStart,
                endSelection = text.selectionEnd,
                hasFocus = hasFocus
            )
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

        text.onSelectionChanged = { startSelection, endSelection ->
            listener.onItemSelectionChanged(
                position = adapterPosition,
                startSelection = startSelection,
                endSelection = endSelection,
                hasFocus = text.hasFocus()
            )
        }

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
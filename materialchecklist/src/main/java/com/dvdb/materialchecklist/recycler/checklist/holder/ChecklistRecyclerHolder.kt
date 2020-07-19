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

package com.dvdb.materialchecklist.recycler.checklist.holder

import android.annotation.SuppressLint
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
import com.dvdb.materialchecklist.config.model.DragAndDropToggleBehavior
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.checklist.listener.ChecklistRecyclerHolderItemListener
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.checklist.model.ChecklistRecyclerItem
import com.dvdb.materialchecklist.recycler.util.holder.DraggableRecyclerHolder
import com.dvdb.materialchecklist.recycler.util.holder.EnterActionPerformedFactory
import com.dvdb.materialchecklist.recycler.util.holder.RequestFocusRecyclerHolder
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
    enterActionActionFactory: EnterActionPerformedFactory,
    private val listener: ChecklistRecyclerHolderItemListener
) : BaseRecyclerHolder<ChecklistRecyclerItem, ChecklistRecyclerHolderConfig>(itemView, config),
    DraggableRecyclerHolder,
    RequestFocusRecyclerHolder {

    private val defaultBackground: Drawable? = itemView.background

    private val dragIndicatorIcon: ImageView = itemView.item_checklist_drag_indicator
    private val checkbox: CheckboxWidget = itemView.item_checklist_checkbox
    private val text: EditTextWidget = itemView.item_checklist_text
    private val deleteIcon: ImageView = itemView.item_checklist_delete

    init {
        initialiseView()
        initListeners(enterActionActionFactory)
    }

    override fun bindView(item: ChecklistRecyclerItem) {
        dragIndicatorIcon.setVisible(
            !item.isChecked && config.dragAndDropToggleBehavior != DragAndDropToggleBehavior.NONE,
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

    override fun requestFocus(
        selectionPosition: Int,
        isShowKeyboard: Boolean
    ) {
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
        val topAndBottomPadding = config.topAndBottomPadding.toInt()
        val leftAndRightPadding = config.leftAndRightPadding?.toInt() ?: itemView.paddingLeft
        itemView.setPadding(
            leftAndRightPadding,
            topAndBottomPadding,
            leftAndRightPadding,
            topAndBottomPadding
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialiseDragIndicator() {
        val tintedIcon = config.iconDragIndicator
        tintedIcon?.setTintCompat(config.iconTintColor)
        dragIndicatorIcon.setImageDrawable(tintedIcon)

        dragIndicatorIcon.alpha = config.iconAlphaDragIndicator

        when (config.dragAndDropToggleBehavior) {
            DragAndDropToggleBehavior.ON_TOUCH -> {
                dragIndicatorIcon.setOnTouchListener { _, event ->
                    if (event.action == MotionEvent.ACTION_DOWN) {
                        listener.onItemDragHandledClicked(adapterPosition)
                        return@setOnTouchListener true
                    }
                    return@setOnTouchListener false
                }

                dragIndicatorIcon.setOnLongClickListener(null)
            }

            DragAndDropToggleBehavior.ON_LONG_CLICK -> {
                dragIndicatorIcon.setOnLongClickListener {
                    listener.onItemDragHandledClicked(adapterPosition)
                    return@setOnLongClickListener true
                }

                dragIndicatorIcon.setOnTouchListener { _, _ -> false }
            }

            DragAndDropToggleBehavior.NONE -> {
            }
        }
    }

    private fun initialiseCheckbox() {
        config.checkboxTintColor?.let {
            CompoundButtonCompat.setButtonTintList(
                checkbox,
                ColorStateList.valueOf(it)
            )
        }
    }

    private fun initialiseText() {
        text.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            config.textSize
        )

        config.textTypeFace?.let {
            text.typeface = it
        }
    }

    private fun initialiseDelete() {
        val tintedIcon = config.iconDelete
        tintedIcon?.setTintCompat(config.iconTintColor)
        deleteIcon.setImageDrawable(tintedIcon)

        deleteIcon.alpha = config.iconAlphaDelete
    }

    private fun initListeners(enterActionActionFactory: EnterActionPerformedFactory) {
        initCheckboxListener()
        initTextListeners(enterActionActionFactory)
        initDeleteIconListener()
    }

    private fun initCheckboxListener() {
        checkbox.setOnCheckedChangeListener { _, isChecked ->
            listener.onItemChecked(
                adapterPosition,
                isChecked
            )
        }
    }

    private fun initTextListeners(enterActionActionFactory: EnterActionPerformedFactory) {
        text.addTextChangedListener(
            object : SimpleTextChangedListener() {
                override fun afterTextChanged(s: Editable?) {
                    if (text.isFocused) {
                        s?.let { text ->
                            listener.onItemTextChanged(
                                adapterPosition,
                                text.toString()
                            )
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

    private fun initDeleteIconListener() {
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
package com.dvdb.checklist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.manager.ChecklistManager
import com.dvdb.checklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.checklist.recycler.adapter.config.ChecklistItemAdapterConfig
import com.dvdb.checklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklist.config.ChecklistRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklistnew.config.ChecklistNewRecyclerHolderConfig
import com.dvdb.checklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.checklist.util.getColorCompat
import com.dvdb.checklist.util.hideKeyboard

class Checklist(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    private val checklistManager: ChecklistManager = ChecklistManager(
        hideKeyboard = {
            hideKeyboard()
            requestFocus()
        }
    )

    init {
        initLayout()

        val recyclerView = createRecyclerView(createConfig(context, attrs))
        addView(recyclerView)

        checklistManager.adapter = recyclerView.adapter as ChecklistItemAdapter
        checklistManager.scrollToPosition = { position ->
            if (position != RecyclerView.NO_POSITION) {
                recyclerView.layoutManager?.scrollToPosition(position)
            }
        }

        if (isInEditMode) {
            setItems(
                "[ ] Slack meeting notes to team\n" +
                        "[ ] Order flowers for girlfriend\n" +
                        "[ ] Organise vacation photos\n" +
                        "[ ] Book flights to Dubai\n" +
                        "[x] Airbnb holiday home"
            )
        }
    }

    fun setItems(formattedText: String) {
        checklistManager.setItems(formattedText)
    }

    fun getFormattedTextItems(): String {
        return checklistManager.getFormattedTextItems()
    }

    private fun initLayout() {
        isFocusableInTouchMode = true
        requestFocus()
    }

    private fun createRecyclerView(config: ChecklistItemAdapterConfig): RecyclerView {
        val recyclerView = RecyclerView(context)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.adapter = ChecklistItemAdapter(
            config,
            ChecklistRecyclerHolder.Factory(
                EnterActionPerformedFactory(),
                checklistManager
            ),
            ChecklistNewRecyclerHolder.Factory(checklistManager.onNewChecklistListItemClicked)
        )

        return recyclerView
    }

    private fun createConfig(
        context: Context,
        attrs: AttributeSet?
    ): ChecklistItemAdapterConfig {
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.Checklist)

        val textColor = attributes.getColor(
            R.styleable.Checklist_text_color,
            context.getColorCompat(R.color.cl_text_checklist_item_text_light)
        )

        val textSize = attributes.getDimension(
            R.styleable.Checklist_text_size_px,
            context.resources.getDimension(R.dimen.item_checklist_text_size)
        )

        val textNewItem = attributes.getString(
            R.styleable.Checklist_text_new_item
        ) ?: context.getString(R.string.item_checklist_new_text)

        val textAlphaCheckedItem = attributes.getFloat(
            R.styleable.Checklist_text_alpha_checked_item,
            0.4F
        )

        val textAlphaNewItem = attributes.getFloat(
            R.styleable.Checklist_text_alpha_new_item,
            0.5F
        )

        val iconTintColor = attributes.getColor(
            R.styleable.Checklist_icon_tint_color,
            context.getColorCompat(R.color.cl_icon_tint_light)
        )

        val iconAlphaDragIndicator = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_drag_indicator,
            0.5F
        )

        val iconAlphaDelete = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_delete,
            0.9F
        )

        val iconAlphaAdd = attributes.getFloat(
            R.styleable.Checklist_icon_alpha_add,
            0.7F
        )

        val checkboxTintColor = attributes.getColor(
            R.styleable.Checklist_checkbox_tint_color,
            0
        ).run { if (this == 0) null else this }

        val checkboxAlphaCheckedItem = attributes.getFloat(
            R.styleable.Checklist_checkbox_alpha_checked_item,
            0.4F
        )

        try {
            return ChecklistItemAdapterConfig(
                checklistConfig = ChecklistRecyclerHolderConfig(
                    textColor = textColor,
                    textSize = textSize,
                    textAlphaCheckedItem = textAlphaCheckedItem,
                    iconTintColor = iconTintColor,
                    iconAlphaDragIndicator = iconAlphaDragIndicator,
                    iconAlphaDelete = iconAlphaDelete,
                    checkboxAlphaCheckedItem = checkboxAlphaCheckedItem,
                    checkboxTintColor = checkboxTintColor
                ),
                checklistNewConfig = ChecklistNewRecyclerHolderConfig(
                    text = textNewItem,
                    textColor = textColor,
                    textSize = textSize,
                    textAlpha = textAlphaNewItem,
                    iconTintColor = iconTintColor,
                    iconAlphaAdd = iconAlphaAdd
                )
            )
        } finally {
            attributes.recycle()
        }
    }
}

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

    private val checklistManager: ChecklistManager = ChecklistManager {
        hideKeyboard()
        requestFocus()
    }

    init {
        initLayout()

        val recyclerView = createRecyclerView(createConfig(context, attrs))
        addView(recyclerView)

        checklistManager.adapter = recyclerView.adapter as ChecklistItemAdapter

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

        val textColorUncheckedItem = attributes.getColor(
            R.styleable.Checklist_text_color_unchecked,
            context.getColorCompat(R.color.cl_text_checklist_item_text_light)
        )

        val textColorCheckedItem = attributes.getColor(
            R.styleable.Checklist_text_color_checked,
            context.getColorCompat(R.color.cl_text_checklist_item_text_checked_light)
        )

        val textColorNewItem = attributes.getColor(
            R.styleable.Checklist_text_color_add_new,
            context.getColorCompat(R.color.cl_text_checklist_item_new_text_light)
        )

        val textSize = attributes.getDimension(
            R.styleable.Checklist_text_size_px,
            context.resources.getDimension(R.dimen.item_checklist_text_size)
        )

        val textNewItem = attributes.getString(
            R.styleable.Checklist_text_new_item
        ) ?: context.getString(R.string.item_checklist_new_text)

        val imageTintColor = attributes.getColor(
            R.styleable.Checklist_image_tint_color,
            context.getColorCompat(R.color.cl_image_tint_light)
        )

        val imageTintColorLight = attributes.getColor(
            R.styleable.Checklist_image_tint_color_light,
            context.getColorCompat(R.color.cl_image_tint_variant_light)
        )

        try {
            return ChecklistItemAdapterConfig(
                checklistConfig = ChecklistRecyclerHolderConfig(
                    textSize,
                    textColorUncheckedItem,
                    textColorCheckedItem,
                    imageTintColor,
                    imageTintColorLight
                ),
                checklistNewConfig = ChecklistNewRecyclerHolderConfig(
                    textNewItem,
                    textSize,
                    textColorNewItem,
                    imageTintColorLight
                )
            )
        } finally {
            attributes.recycle()
        }
    }
}

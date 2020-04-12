package com.dvdb.checklist

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.checklist.manager.ChecklistManager
import com.dvdb.checklist.recycler.adapter.ChecklistItemAdapter
import com.dvdb.checklist.recycler.holder.checklist.ChecklistRecyclerHolder
import com.dvdb.checklist.recycler.holder.checklistnew.ChecklistNewRecyclerHolder
import com.dvdb.checklist.recycler.holder.util.EnterActionPerformedFactory
import com.dvdb.checklist.recycler.util.ItemTouchHelperAdapter
import com.dvdb.checklist.recycler.util.SimpleItemTouchHelper
import com.dvdb.checklist.util.hideKeyboard

class Checklist(
    context: Context,
    attrs: AttributeSet?
) : FrameLayout(context, attrs) {

    private val manager: ChecklistManager = ChecklistManager(
        hideKeyboard = {
            hideKeyboard()
            requestFocus()
        }
    )

    private val config: ChecklistConfig = ChecklistConfig(
        context = context,
        attrs = attrs
    )

    init {
        initLayout()

        val recyclerView = createRecyclerView()
        addView(recyclerView)

        val itemTouchCallback = SimpleItemTouchHelper(recyclerView.adapter as ItemTouchHelperAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        initManager(recyclerView, itemTouchHelper, itemTouchCallback)

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
        manager.setItems(formattedText)
    }

    fun getFormattedTextItems(
        keepCheckedItems: Boolean = true,
        skipCheckedItems: Boolean = false
    ): String {
        return manager.getFormattedTextItems(
            keepCheckedItems,
            skipCheckedItems
        )
    }

    private fun initLayout() {
        isFocusableInTouchMode = true
        requestFocus()
    }

    private fun createRecyclerView(): RecyclerView {
        val recyclerView = RecyclerView(context)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.overScrollMode = View.OVER_SCROLL_NEVER
        recyclerView.adapter = ChecklistItemAdapter(
            config = config.toAdapterConfig(),
            itemRecyclerHolderFactory = ChecklistRecyclerHolder.Factory(
                EnterActionPerformedFactory(),
                manager
            ),
            itemNewRecyclerHolderFactory = ChecklistNewRecyclerHolder.Factory(
                manager.onNewChecklistListItemClicked
            ),
            itemDragListener = manager
        )

        return recyclerView
    }

    private fun initManager(
        recyclerView: RecyclerView,
        itemTouchHelper: ItemTouchHelper,
        itemTouchCallback: SimpleItemTouchHelper
    ) {
        manager.lateInitState(
            adapter = recyclerView.adapter as ChecklistItemAdapter,
            config = config.toManagerConfig(),
            scrollToPosition = { position ->
                if (position != RecyclerView.NO_POSITION) {
                    recyclerView.layoutManager?.scrollToPosition(position)
                }
            },
            startDragAndDrop = { position ->
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            },
            enableDragAndDrop = { isEnabled ->
                itemTouchCallback.setIsDragEnabled(isEnabled)
            }
        )
    }
}

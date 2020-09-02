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

package com.dvdb.materialchecklist.recycler.title.holder

import android.text.Editable
import android.text.util.Linkify
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.title.listener.TitleRecyclerHolderItemListener
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.title.model.TitleRecyclerItem
import com.dvdb.materialchecklist.recycler.util.holder.RequestFocusRecyclerHolder
import com.dvdb.materialchecklist.util.*
import kotlinx.android.synthetic.main.item_title.view.*

internal class TitleRecyclerHolder private constructor(
    itemView: View,
    config: TitleRecyclerHolderConfig,
    private val listener: TitleRecyclerHolderItemListener
) : BaseRecyclerHolder<TitleRecyclerItem, TitleRecyclerHolderConfig>(itemView, config),
    RequestFocusRecyclerHolder {

    private val text: EditText = itemView.item_title_text
    private val actionIcon: ImageView = itemView.item_title_action_icon

    init {
        initialiseView()
        initListeners()
    }

    override fun bindView(item: TitleRecyclerItem) {
        text.setText(item.text)

        if (config.isLinksClickable) {
            Linkify.addLinks(text.text, Linkify.ALL)
        }
    }

    override fun onConfigUpdated() {
        initialiseView()
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
        initText()
        initActionIcon()
    }

    private fun initialiseRoot() {
        val leftPadding = config.leftPadding?.toInt() ?: itemView.paddingLeft
        val rightPadding = config.rightPadding?.toInt() ?: itemView.paddingRight
        val topAndBottomPadding = config.topAndBottomPadding.toInt()
        itemView.setPadding(
            leftPadding,
            topAndBottomPadding,
            rightPadding,
            topAndBottomPadding
        )
    }

    private fun initText() {
        initTextAppearance()
        initTextBehavior()
    }

    private fun initTextAppearance() {
        text.hint = config.hint
        text.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            config.textSize
        )
        text.setTypeface(config.typeFace, config.typeFaceStyle)

        text.setTextColor(config.textColor)
        text.setLinkTextColor(config.linkTextColor)

        config.hintTextColor?.let {
            text.setHintTextColor(it)
        }
    }

    private fun initTextBehavior() {
        text.isFocusableInTouchMode = config.isEditable
        text.isClickable = config.isEditable
        text.isLongClickable = config.isEditable
        text.linksClickable = config.isEditable and config.isLinksClickable

        if (config.isAddClickableLinks) {
            text.movementMethod = LinksMovementMethod
        }

        if (!config.isEditable) {
            text.clearFocus()
        }
    }

    private fun initActionIcon() {
        val tintedIcon = config.actionIcon
        tintedIcon?.setTintCompat(config.iconTintColor)
        actionIcon.setImageDrawable(tintedIcon)

        actionIcon.setVisible(config.isShowActionIcon)
    }

    private fun initListeners() {
        initTextListeners()
        initActionIconListener()
    }

    private fun initTextListeners() {
        text.addTextChangedListener(object : SimpleTextChangedListener() {
            override fun afterTextChanged(s: Editable?) {
                if (text.isFocused) {
                    s?.let { text ->
                        listener.onTitleItemTextChanged(
                            adapterPosition,
                            text.toString()
                        )
                    }
                }
            }
        })

        text.setOnFocusChangeListener { _, hasFocus ->
            listener.onTitleItemFocusChanged(
                position = adapterPosition,
                startSelection = text.selectionStart,
                endSelection = text.selectionEnd,
                hasFocus = hasFocus
            )
        }
    }

    private fun initActionIconListener() {
        actionIcon.setOnClickListener {
            listener.onTitleItemActionIconClicked(adapterPosition)
        }
    }

    class Factory(
        private val listener: TitleRecyclerHolderItemListener
    ) : BaseRecyclerHolderFactory<TitleRecyclerItem, TitleRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: TitleRecyclerHolderConfig
        ): BaseRecyclerHolder<TitleRecyclerItem, TitleRecyclerHolderConfig> {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_title,
                parent,
                false
            )

            return TitleRecyclerHolder(
                itemView,
                config,
                listener
            )
        }
    }
}
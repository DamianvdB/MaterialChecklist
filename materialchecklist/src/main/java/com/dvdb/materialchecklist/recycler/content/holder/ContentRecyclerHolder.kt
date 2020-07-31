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

package com.dvdb.materialchecklist.recycler.content.holder

import ContentRecyclerHolderItemListener
import android.content.Context
import android.text.Editable
import android.text.util.Linkify
import android.util.TypedValue
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolder
import com.dvdb.materialchecklist.recycler.base.holder.BaseRecyclerHolderFactory
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.content.model.ContentRecyclerItem
import com.dvdb.materialchecklist.recycler.util.holder.RequestFocusRecyclerHolder
import com.dvdb.materialchecklist.util.LinksMovementMethod
import com.dvdb.materialchecklist.util.SimpleTextChangedListener
import com.dvdb.materialchecklist.util.showKeyboard

internal class ContentRecyclerHolder private constructor(
    private val text: EditText,
    config: ContentRecyclerHolderConfig,
    private val listener: ContentRecyclerHolderItemListener
) : BaseRecyclerHolder<ContentRecyclerItem, ContentRecyclerHolderConfig>(text, config),
    RequestFocusRecyclerHolder {

    init {
        initialiseView()
        initTextListeners()
    }

    override fun bindView(item: ContentRecyclerItem) {
        text.setText(item.text)

        addLinksIfEnabled()
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
    }

    private fun initialiseRoot() {
        val leftAndRightPadding = config.leftAndRightPadding.toInt()
        itemView.setPadding(
            leftAndRightPadding,
            config.topPadding.toInt(),
            leftAndRightPadding,
            config.bottomPadding.toInt()
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
        text.typeface = config.typeFace

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

        if (config.isLinksClickable) {
            text.movementMethod = LinksMovementMethod
        }

        if (!config.isEditable) {
            text.clearFocus()
        }
    }

    private fun initTextListeners() {
        text.addTextChangedListener(object : SimpleTextChangedListener() {
            override fun afterTextChanged(s: Editable?) {
                if (text.isFocused) {
                    s?.let { text ->
                        listener.onContentItemTextChanged(
                            adapterPosition,
                            text.toString()
                        )
                    }

                    addLinksIfEnabled()
                }
            }
        })

        text.setOnFocusChangeListener { _, hasFocus ->
            listener.onContentItemFocusChanged(
                position = adapterPosition,
                startSelection = text.selectionStart,
                endSelection = text.selectionEnd,
                hasFocus = hasFocus
            )
        }
    }

    private fun addLinksIfEnabled() {
        if (config.isLinksClickable) {
            Linkify.addLinks(text.text, Linkify.ALL)
        }
    }

    class Factory(
        private val listener: ContentRecyclerHolderItemListener
    ) : BaseRecyclerHolderFactory<ContentRecyclerItem, ContentRecyclerHolderConfig> {

        override fun create(
            parent: ViewGroup,
            config: ContentRecyclerHolderConfig
        ): BaseRecyclerHolder<ContentRecyclerItem, ContentRecyclerHolderConfig> {
            return ContentRecyclerHolder(
                createEditText(parent.context),
                config,
                listener
            )
        }

        private fun createEditText(context: Context): EditText {
            val editText = EditText(context)

            editText.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            editText.setLineSpacing(
                0f,
                ResourcesCompat.getFloat(
                    context.resources,
                    R.dimen.mc_item_content_line_spacing_multiplier
                )
            )

            editText.setBackgroundResource(android.R.color.transparent)

            editText.inputType = EditorInfo.TYPE_CLASS_TEXT or
                    EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE or
                    EditorInfo.TYPE_TEXT_FLAG_CAP_SENTENCES

            return editText
        }
    }
}
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

package com.dvdb.materialchecklist.widget

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputConnectionWrapper
import androidx.appcompat.widget.AppCompatEditText

/**
 * Edit Text widget that avoids carriage-return and line-feed while in multi-line mode.
 *
 * Additionally, provides listeners for:
 * 1. Changes in the current selection positioning.
 * 2. Delete key presses via the input connection.
 */
internal class MultiLineEditTextWidget(
    context: Context,
    attrs: AttributeSet?
) : AppCompatEditText(context, attrs) {

    var onSelectionChanged: ((startSelection: Int, endSelection: Int) -> Unit)? = null
    var onDeleteKeyPressed: (() -> Unit)? = null

    init {
        imeOptions = EditorInfo.IME_ACTION_NEXT
        inputType = inputType or InputType.TYPE_TEXT_FLAG_MULTI_LINE
    }

    override fun onSelectionChanged(
        selStart: Int,
        selEnd: Int
    ) {
        super.onSelectionChanged(selStart, selEnd)
        onSelectionChanged?.invoke(selStart, selEnd)
    }

    override fun onCreateInputConnection(outAttrs: EditorInfo): InputConnection {
        val connection = super.onCreateInputConnection(outAttrs)

        // Revert removal of actions such as NEXT, DONE, etc, when setting inputType="textMultiLine"
        if (outAttrs.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION != 0) {
            outAttrs.imeOptions = outAttrs.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION.inv()
        }

        return DeleteKeyPressedInputConnectionWrapper(connection, true)
    }

    inner class DeleteKeyPressedInputConnectionWrapper(
        target: InputConnection?,
        mutable: Boolean
    ) : InputConnectionWrapper(target, mutable) {

        override fun sendKeyEvent(event: KeyEvent?): Boolean {
            if (event?.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_DEL &&
                this@MultiLineEditTextWidget.selectionStart == 0 &&
                this@MultiLineEditTextWidget.selectionEnd == 0
            ) {
                onDeleteKeyPressed?.invoke()
            }
            return super.sendKeyEvent(event)
        }
    }
}
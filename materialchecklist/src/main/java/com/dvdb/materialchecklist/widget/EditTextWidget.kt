package com.dvdb.materialchecklist.widget

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import androidx.appcompat.widget.AppCompatEditText

/**
 * Edit Text widget that avoids carriage-return and line-feed while in multi-line mode.
 */
internal class EditTextWidget(
    context: Context,
    attrs: AttributeSet?
) : AppCompatEditText(context, attrs) {

    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
        val connection = super.onCreateInputConnection(outAttrs)

        // Revert removal of actions such as NEXT, DONE, etc, when setting inputType="textMultiLine"
        outAttrs?.let {
            if (it.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION != 0) {
                it.imeOptions = it.imeOptions and EditorInfo.IME_FLAG_NO_ENTER_ACTION.inv()
            }
        }

        return connection
    }
}
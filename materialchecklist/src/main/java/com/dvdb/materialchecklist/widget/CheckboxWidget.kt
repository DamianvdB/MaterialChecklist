package com.dvdb.materialchecklist.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

/**
 * Checkbox widget that allows setting the is checked flag without invoking the on checked change listener.
 */
internal class CheckboxWidget(
    context: Context,
    attrs: AttributeSet?
) : AppCompatCheckBox(context, attrs) {

    private var checkedChangeListener: OnCheckedChangeListener? = null

    override fun setOnCheckedChangeListener(listener: OnCheckedChangeListener?) {
        checkedChangeListener = listener
        super.setOnCheckedChangeListener(listener)
    }

    fun setChecked(checked: Boolean, notify: Boolean) {
        if (!notify) {
            super.setOnCheckedChangeListener(null)
            super.setChecked(checked)
            super.setOnCheckedChangeListener(checkedChangeListener)
            return
        }
        super.setChecked(checked)
    }
}
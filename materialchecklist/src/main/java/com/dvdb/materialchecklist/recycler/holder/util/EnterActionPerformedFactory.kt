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

package com.dvdb.materialchecklist.recycler.holder.util

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView

internal class EnterActionPerformedFactory {

    fun create(
        runnable: () -> Boolean,
        preConditions: () -> Boolean = { false }
    ) = TextView.OnEditorActionListener { _, actionId, event ->
        if (preConditions()) {
            return@OnEditorActionListener false
        }

        if (event == null) {
            if (actionId != EditorInfo.IME_ACTION_NEXT && actionId != EditorInfo.IME_ACTION_DONE) {
                return@OnEditorActionListener false
            }
        } else if (actionId == EditorInfo.IME_NULL || actionId == KeyEvent.KEYCODE_ENTER) {
            if (event.action != KeyEvent.ACTION_DOWN) {
                return@OnEditorActionListener true
            }
        } else {
            return@OnEditorActionListener false
        }

        return@OnEditorActionListener runnable()
    }
}
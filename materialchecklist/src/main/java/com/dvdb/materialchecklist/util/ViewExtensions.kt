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

package com.dvdb.materialchecklist.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.dvdb.materialchecklist.util.imageloader.ImageLoader

internal fun View.setVisible(
    isVisible: Boolean,
    hiddenVisibility: Int = View.GONE
) {
    require(hiddenVisibility == View.GONE || hiddenVisibility == View.INVISIBLE)
    visibility = if (isVisible) View.VISIBLE else hiddenVisibility
}

internal fun View.showKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

internal fun View.hideKeyboard() {
    (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
        ?.hideSoftInputFromWindow(windowToken, 0)
}

internal inline fun View.updateLayoutParams(block: ViewGroup.LayoutParams.() -> Unit) {
    val params = layoutParams
    block(params)
    layoutParams = params
}

internal fun ImageView.loadImage(
    uri: Uri,
    onLoadSuccess: (Drawable?) -> Unit = {},
    onLoadFailed: () -> Unit = {}
) {
    ImageLoader.loadImage(
        this,
        uri,
        onLoadSuccess,
        onLoadFailed
    )
}
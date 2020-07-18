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

package com.dvdb.materialchecklist.recycler.imagecontainer.image.holder

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dvdb.materialchecklist.R
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerHolderConfig
import com.dvdb.materialchecklist.recycler.imagecontainer.image.model.ImageRecyclerItem
import com.dvdb.materialchecklist.util.loadImage
import com.dvdb.materialchecklist.util.setVisible
import kotlinx.android.synthetic.main.item_image.view.*

internal class ImageRecyclerHolder private constructor(
    itemView: View,
    private var config: ImageRecyclerHolderConfig,
    private val onItemClicked: (position: Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val primaryImage: ImageView = itemView.item_image_primary_image
    private val secondaryImage: ImageView = itemView.item_image_secondary_image
    private val text: TextView = itemView.item_image_text

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.outlineProvider = ViewOutlineProvider.BACKGROUND
            itemView.clipToOutline = true
        }

        initialiseView()
        initClickListener(itemView)
    }

    fun bindView(item: ImageRecyclerItem) {
        bindText(item)
        bindPrimaryImage(item)
        bindSecondaryImage(item)
    }

    fun updateConfigConditionally(config: ImageRecyclerHolderConfig) {
        if (this.config != config) {
            this.config = config
            initialiseView()
        }
    }

    private fun initialiseView() {
        initRoot()
        initText()
    }

    private fun initRoot() {
        itemView.background = GradientDrawable().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cornerRadius = config.cornerRadius
            }

            setStroke(
                config.strokeWidth,
                config.strokeColor
            )
        }
    }

    private fun initText() {
        text.setTextColor(config.textColor)
        text.maxLines = config.textMaxLines
        text.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            config.textSize
        )
        text.typeface = config.typeFace
    }

    private fun initClickListener(itemView: View) {
        itemView.setOnClickListener {
            onItemClicked(adapterPosition)
        }
    }

    private fun bindText(item: ImageRecyclerItem) {
        text.text = item.text
        text.setVisible(item.shouldShowText)
    }

    private fun bindPrimaryImage(item: ImageRecyclerItem) {
        primaryImage.bindImage(
            isImageDrawableSet = item.isPrimaryImageDrawableSet,
            drawable = item.primaryImage,
            isImageUriSet = item.isPrimaryImageUriSet,
            uri = item.primaryImageUri
        )
    }

    private fun bindSecondaryImage(item: ImageRecyclerItem) {
        secondaryImage.bindImage(
            isImageDrawableSet = item.isSecondaryImageDrawableSet,
            drawable = item.secondaryImage,
            isImageUriSet = item.isSecondaryImageUriSet,
            uri = item.secondaryImageUri
        )
    }

    private fun ImageView.bindImage(
        isImageDrawableSet: Boolean,
        drawable: Drawable?,
        isImageUriSet: Boolean,
        uri: Uri
    ) {
        if (isImageDrawableSet) {
            setImageDrawable(drawable)
        } else if (isImageUriSet) {
            loadImage(uri)
        }

        setVisible(
            isImageDrawableSet or isImageUriSet,
            View.INVISIBLE
        )
    }

    class Factory(
        private val onItemClicked: (position: Int) -> Unit
    ) {
        fun create(
            parent: ViewGroup,
            config: ImageRecyclerHolderConfig
        ): ImageRecyclerHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.item_image,
                parent,
                false
            )

            return ImageRecyclerHolder(
                itemView,
                config,
                onItemClicked
            )
        }
    }
}
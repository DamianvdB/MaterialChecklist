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

package com.dvdb.checklist.sample

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dvdb.checklist.R
import com.dvdb.materialchecklist.config.*
import com.dvdb.materialchecklist.manager.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val SETTINGS_ACTIVITY_REQUEST_CODE = 1000

private const val SP_CHECKLIST_ITEMS_TEXT_KEY = "mc_items_text"

private const val SP_SHOW_CHECKLIST_KEY = "mc_show_checklist"

private const val CHECKLIST_ITEMS_SAMPLE_TEXT = "[ ] Send meeting notes to team\n" +
        "[ ] Order flowers\n" +
        "[ ] Organise vacation photos\n" +
        "[ ] Book holiday flights\n" +
        "[ ] Scan vaccination certificates\n" +
        "[x] Advertise holiday home\n" +
        "[x] Wish Sarah happy birthday"

internal class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var checklistConfiguration: ChecklistConfiguration

    private var checklistItemsText: String = CHECKLIST_ITEMS_SAMPLE_TEXT
        get() = sharedPreferences.getString(SP_CHECKLIST_ITEMS_TEXT_KEY, field) ?: field
        set(value) {
            field = value
            sharedPreferences.edit().putString(SP_CHECKLIST_ITEMS_TEXT_KEY, value).apply()
        }

    private var showChecklist: Boolean = true
        get() = sharedPreferences.getBoolean(SP_SHOW_CHECKLIST_KEY, field)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(SP_SHOW_CHECKLIST_KEY, value).apply()
        }

    private lateinit var convertToTextMenuItem: MenuItem
    private lateinit var convertToChecklistMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        checklistConfiguration = ChecklistConfiguration(this, sharedPreferences)

        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)

        convertToTextMenuItem = menu!!.findItem(R.id.menu_main_activity_convert_to_text).apply {
            isVisible = showChecklist
        }
        convertToChecklistMenuItem = menu.findItem(R.id.menu_main_activity_convert_to_checklist).apply {
            isVisible = !showChecklist
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_activity_convert_to_text -> handleOnConvertToTextMenuItemClicked()
            R.id.menu_main_activity_convert_to_checklist -> handleOnConvertToChecklistMenuItemClicked()
            R.id.menu_main_activity_remove_checked_items -> handleOnRemoveCheckedItemsMenuItemClicked()
            R.id.menu_main_activity_uncheck_checked_items -> handleOnUncheckCheckedItemsMenuItemClicked()
            R.id.menu_main_activity_github -> handleOnGithubMenuItemClicked()
            R.id.menu_main_activity_settings -> handleOnSettingsMenuItemClicked()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {
            handleSettingChecklistConfiguration()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        checklistItemsText = if (showChecklist) main_checklist.getFormattedTextItems() else main_text.text.toString()
        super.onStop()
    }

    private fun initView() {
        initChecklist()

        if (showChecklist) {
            main_checklist.setItems(checklistItemsText)
        } else {
            main_text.setText(checklistItemsText)

            main_text.visibility = View.VISIBLE
            main_checklist.visibility = View.GONE
        }

        handleSettingChecklistConfiguration()
    }

    private fun initChecklist() {
        main_checklist.setOnItemDeletedListener { text, id ->
            val message: String = if (text.isNotEmpty()) "Checklist item deleted: \"$text\"" else "Checklist item deleted"
            Snackbar.make(main_root, message, Snackbar.LENGTH_LONG)
                .setAction("undo") {
                    main_checklist.restoreDeletedItem(id)
                }.show()
        }
    }

    private fun handleSettingChecklistConfiguration() {
        // Apply text settings from config to checklist system
        main_checklist.setTextColor(checklistConfiguration.textColor)
            .setTextSize(checklistConfiguration.textSize)
            .setNewItemText(checklistConfiguration.textNewItem)
            .setCheckedItemTextAlpha(checklistConfiguration.textAlphaCheckedItem)
            .setNewItemTextAlpha(checklistConfiguration.textAlphaNewItem)

        checklistConfiguration.textTypeFace?.let {
            main_checklist.setTextTypeFace(it)
        }

        // Apply icon settings from config to checklist system
        main_checklist.setIconTintColor(checklistConfiguration.iconTintColor)
            .setDragIndicatorIconAlpha(checklistConfiguration.iconAlphaDragIndicator)
            .setDeleteIconAlpha(checklistConfiguration.iconAlphaDelete)
            .setAddIconAlpha(checklistConfiguration.iconAlphaAdd)

        // Apply checkbox settings from config to checklist system
        checklistConfiguration.checkboxTintColor?.let {
            main_checklist.setCheckboxTintColor(it)
        }

        main_checklist.setCheckedItemCheckboxAlpha(checklistConfiguration.checkboxAlphaCheckedItem)

        // Apply drag-and-drop settings from config to checklist system
        main_checklist.setDragAndDropToggleMode(checklistConfiguration.dragAndDropToggleMode)

        checklistConfiguration.dragAndDropActiveItemBackgroundColor?.let {
            main_checklist.setDragAndDropItemActiveBackgroundColor(it)
        }

        // Apply item behavior settings from config to checklist system
        main_checklist.setOnItemCheckedBehavior(checklistConfiguration.behaviorCheckedItem)
            .setOnItemUncheckedBehavior(checklistConfiguration.behaviorUncheckedItem)

        // Apply item settings from config to checklist system
        if (checklistConfiguration.itemFirstTopPadding != null ||
            checklistConfiguration.itemLeftAndRightPadding != null ||
            checklistConfiguration.itemLastBottomPadding != null
        ) {
            main_checklist.setItemPadding(
                firstItemTopPadding = checklistConfiguration.itemFirstTopPadding,
                leftAndRightPadding = checklistConfiguration.itemLeftAndRightPadding,
                lastItemBottomPadding = checklistConfiguration.itemLastBottomPadding
            )
        }
    }

    private fun handleOnConvertToChecklistMenuItemClicked() {
        updateVisibleContentOnConvertMenuItemClicked(true)
    }

    private fun handleOnConvertToTextMenuItemClicked() {
        updateVisibleContentOnConvertMenuItemClicked(false)
    }

    private fun updateVisibleContentOnConvertMenuItemClicked(isConvertToChecklistMenuItemClicked: Boolean) {
        showChecklist = isConvertToChecklistMenuItemClicked

        convertToTextMenuItem.isVisible = isConvertToChecklistMenuItemClicked
        convertToChecklistMenuItem.isVisible = !isConvertToChecklistMenuItemClicked

        if (isConvertToChecklistMenuItemClicked) {
            val content: String = main_text.text.toString()
            main_checklist.setItems(content)
        } else {
            val content: String = main_checklist.getFormattedTextItems()
            main_text.setText(content)
        }

        main_text.visibility = if (isConvertToChecklistMenuItemClicked) View.GONE else View.VISIBLE
        main_checklist.visibility = if (isConvertToChecklistMenuItemClicked) View.VISIBLE else View.GONE
    }

    private fun handleOnRemoveCheckedItemsMenuItemClicked() {
        val itemIdsOfRemovedItems = main_checklist.removeAllCheckedItems()

        val message = resources.getQuantityString(
            R.plurals.item_checked_removed,
            itemIdsOfRemovedItems.size,
            itemIdsOfRemovedItems.size
        )

        Snackbar.make(main_root, message, Snackbar.LENGTH_LONG).apply {
            if (itemIdsOfRemovedItems.isNotEmpty()) {
                setAction("undo") {
                    main_checklist.restoreDeleteItems(itemIdsOfRemovedItems)
                }
            }
        }.show()
    }

    private fun handleOnUncheckCheckedItemsMenuItemClicked() {
        main_checklist.uncheckAllCheckedItems()
    }

    private fun handleOnGithubMenuItemClicked() {
        dismissKeyboard()

        startActivity(
            Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("http://bit.ly/damian_van_den_berg_github_material_checklist"))
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun handleOnSettingsMenuItemClicked() {
        dismissKeyboard()

        startActivityForResult(
            Intent(this, SettingsActivity::class.java),
            SETTINGS_ACTIVITY_REQUEST_CODE
        )
    }

    private fun dismissKeyboard() {
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}

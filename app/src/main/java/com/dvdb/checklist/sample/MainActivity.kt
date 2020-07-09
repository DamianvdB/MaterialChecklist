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
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dvdb.checklist.R
import com.dvdb.checklist.sample.config.ChecklistConfiguration
import com.dvdb.materialchecklist.config.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val SETTINGS_ACTIVITY_REQUEST_CODE = 1000

private const val CHANGE_MENU_ITEM_VISIBILITY_DELAY_MS = 100L

private const val SP_CHECKLIST_ITEMS_TEXT_KEY = "mc_items_text"
private const val SP_SHOW_CHECKLIST_KEY = "mc_show_checklist"

private const val CHECKLIST_ITEMS_SAMPLE_TEXT = "[ ] Send meeting notes to team\n" +
        "[ ] Order flowers\n" +
        "[ ] Organise vacation photos\n" +
        "[ ] Book holiday flights\n" +
        "[ ] Scan vaccination certificates\n" +
        "[x] Advertise holiday home\n" +
        "[x] Wish Sarah happy birthday"

private const val SP_TITLE_ITEM_TEXT_KEY = "mc_item_title_text"

private const val TITLE_ITEM_SAMLPE_TEXT = "Admin Tasks"

private const val D_NOTES_URL = "https://bit.ly/google_play_store_d_notes"
private const val GITHUB_URL = "https://bit.ly/github_material_checklist"

internal class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var checklistConfiguration: ChecklistConfiguration

    private var checklistItemsText: String = CHECKLIST_ITEMS_SAMPLE_TEXT
        get() = sharedPreferences.getString(SP_CHECKLIST_ITEMS_TEXT_KEY, field) ?: field
        set(value) {
            field = value
            sharedPreferences.edit().putString(SP_CHECKLIST_ITEMS_TEXT_KEY, value).apply()
        }

    private var titleItemText: String = TITLE_ITEM_SAMLPE_TEXT
        get() = sharedPreferences.getString(SP_TITLE_ITEM_TEXT_KEY, field) ?: field
        set(value) {
            field = value
            sharedPreferences.edit().putString(SP_TITLE_ITEM_TEXT_KEY, value).apply()
        }

    private var showChecklist: Boolean = true
        get() = sharedPreferences.getBoolean(SP_SHOW_CHECKLIST_KEY, field)
        set(value) {
            field = value
            sharedPreferences.edit().putBoolean(SP_SHOW_CHECKLIST_KEY, value).apply()
        }

    private lateinit var convertToTextMenuItem: MenuItem
    private lateinit var convertToChecklistMenuItem: MenuItem
    private lateinit var removeCheckedItemsMenuItem: MenuItem
    private lateinit var uncheckCheckedItemsMenuItem: MenuItem

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
        removeCheckedItemsMenuItem = menu.findItem(R.id.menu_main_activity_remove_checked_items).apply {
            isVisible = showChecklist
        }
        uncheckCheckedItemsMenuItem = menu.findItem(R.id.menu_main_activity_uncheck_checked_items).apply {
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
            R.id.menu_main_activity_d_notes -> handleOnDNotesMenuItemClicked()
            R.id.menu_main_activity_github -> handleOnGithubMenuItemClicked()
            R.id.menu_main_activity_settings -> handleOnSettingsMenuItemClicked()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_ACTIVITY_REQUEST_CODE) {
            handleSettingConfiguration()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        checklistItemsText = if (showChecklist) main_checklist.getItems() else main_text.text.toString()

        if (showChecklist) {
            main_checklist.getTitleItem()?.let { title ->
                titleItemText = title
            }
        }
        super.onStop()
    }

    private fun initView() {
        initChecklist()
        initTitle()

        if (showChecklist) {
            main_checklist.setItems(checklistItemsText)
            main_checklist.setTitleItem(titleItemText)
        } else {
            main_text.setText(checklistItemsText)

            main_text.visibility = View.VISIBLE
            main_checklist.visibility = View.GONE
        }

        handleSettingConfiguration()
    }

    private fun initChecklist() {
        main_checklist.setOnItemDeletedListener { text, id ->
            val message: String =
                if (text.isNotEmpty()) "Checklist item deleted: \"$text\"" else "Checklist item deleted"
            Snackbar.make(main_root, message, Snackbar.LENGTH_LONG)
                .setAction("undo") {
                    main_checklist.restoreDeletedItem(id)
                }.show()
        }
    }

    private fun initTitle() {
        main_checklist.setOnTitleItemEnterKeyPressed {
            main_checklist.setItemFocusPosition(1)
        }
    }

    private fun handleSettingConfiguration() {
        handleSettingChecklistConfiguration()
        handleSettingTitleConfiguration()
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
        main_checklist.setDragAndDropToggleBehavior(checklistConfiguration.dragAndDropToggleBehavior)
            .setDragAndDropDismissKeyboardBehavior(checklistConfiguration.dragAndDismissKeyboardBehavior)

        checklistConfiguration.dragAndDropActiveItemBackgroundColor?.let {
            main_checklist.setDragAndDropItemActiveBackgroundColor(it)
        }

        // Apply item behavior settings from config to checklist system
        main_checklist.setOnItemCheckedBehavior(checklistConfiguration.behaviorCheckedItem)
            .setOnItemUncheckedBehavior(checklistConfiguration.behaviorUncheckedItem)

        // Apply item settings from config to checklist system
        checklistConfiguration.itemFirstTopPadding?.let {
            main_checklist.setItemFirstTopPadding(it)
        }

        checklistConfiguration.itemLeftAndRightPadding?.let {
            main_checklist.setItemLeftAndRightPadding(it)
        }

        checklistConfiguration.itemTopAndBottomPadding?.let {
            main_checklist.setItemTopAndBottomPadding(it)
        }

        checklistConfiguration.itemLastBottomPadding?.let {
            main_checklist.setItemLastBottomPadding(it)
        }

        // Apply text settings from config to edit text
        main_text.setTextColor(checklistConfiguration.textColor)
        main_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, checklistConfiguration.textSize)
        checklistConfiguration.textTypeFace?.let {
            main_text.typeface = it
        }
    }

    private fun handleSettingTitleConfiguration(){
        main_checklist.setTitleHint(checklistConfiguration.textTitleHint)
            .setTitleTextColor(checklistConfiguration.textTitleColor)
            .setTitleLinkTextColor(checklistConfiguration.textTitleLinkColor)
            .setTitleHintTextColor(checklistConfiguration.textTitleHintColor)
            .setTitleClickableLinks(checklistConfiguration.textTitleClickableLinks)
            .setTitleEditable(checklistConfiguration.textTitleEditable)
            .setTitleShowActionIcon(checklistConfiguration.iconTitleShowAction)
    }

    private fun handleOnConvertToChecklistMenuItemClicked() {
        updateVisibleContentOnConvertMenuItemClicked(true)
    }

    private fun handleOnConvertToTextMenuItemClicked() {
        updateVisibleContentOnConvertMenuItemClicked(false)
    }

    private fun updateVisibleContentOnConvertMenuItemClicked(isConvertToChecklistMenuItemClicked: Boolean) {
        showChecklist = isConvertToChecklistMenuItemClicked

        main_root?.handler?.postDelayed(
            {
                convertToTextMenuItem.isVisible = isConvertToChecklistMenuItemClicked
                removeCheckedItemsMenuItem.isVisible = isConvertToChecklistMenuItemClicked
                uncheckCheckedItemsMenuItem.isVisible = isConvertToChecklistMenuItemClicked

                convertToChecklistMenuItem.isVisible = !isConvertToChecklistMenuItemClicked
            },
            CHANGE_MENU_ITEM_VISIBILITY_DELAY_MS
        )

        if (isConvertToChecklistMenuItemClicked) {
            val content: String = main_text.text.toString()
            main_checklist.setItems(content)

            main_checklist.setTitleItem(titleItemText)
        } else {
            val content: String = main_checklist.getItems()
            main_text.setText(content)

            main_checklist.getTitleItem()?.let { title ->
                titleItemText = title
            }
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

    private fun handleOnDNotesMenuItemClicked() {
        openUrl(D_NOTES_URL)
    }

    private fun handleOnGithubMenuItemClicked() {
        openUrl(GITHUB_URL)
    }

    private fun openUrl(url: String) {
        dismissKeyboard()

        startActivity(
            Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(url))
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

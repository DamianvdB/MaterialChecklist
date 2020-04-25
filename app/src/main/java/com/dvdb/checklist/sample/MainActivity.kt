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
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.dvdb.checklist.R
import com.dvdb.materialchecklist.config.*
import com.dvdb.materialchecklist.manager.getFormattedTextItems
import com.dvdb.materialchecklist.manager.restoreDeletedItem
import com.dvdb.materialchecklist.manager.setItems
import com.dvdb.materialchecklist.manager.setOnItemDeletedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val SETTINGS_ACTIVITY_REQUEST_CODE = 1000

private const val CHECKLIST_ITEMS_TEXT = "[ ] Send meeting notes to team\n" +
        "[ ] Order flowers\n" +
        "[ ] Organise vacation photos\n" +
        "[ ] Book holiday flights\n" +
        "[ ] Scan vaccination certificates\n" +
        "[x] Advertise holiday home\n" +
        "[x] Wish Sarah happy birthday"

private const val SP_CHECKLIST_ITEMS_TEXT_KEY = "cl_items_text"

internal class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var checklistConfiguration: ChecklistConfiguration

    private var checklistItemsText: String = CHECKLIST_ITEMS_TEXT
        get() = sharedPreferences.getString(SP_CHECKLIST_ITEMS_TEXT_KEY, field) ?: field
        set(value) {
            field = value
            sharedPreferences.edit().putString(SP_CHECKLIST_ITEMS_TEXT_KEY, value).apply()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        checklistConfiguration = ChecklistConfiguration(this, sharedPreferences)
        initChecklist()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_activity_settings -> {
                dismissKeyboard()

                startActivityForResult(
                    Intent(this, SettingsActivity::class.java),
                    SETTINGS_ACTIVITY_REQUEST_CODE
                )
            }
            R.id.menu_main_activity_github -> {
                dismissKeyboard()

                startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("http://bit.ly/damian_van_den_berg_github"))
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
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
        checklistItemsText = main_checklist.getFormattedTextItems()
        super.onStop()
    }

    private fun initChecklist() {
        main_checklist.setItems(checklistItemsText)

        main_checklist.setOnItemDeletedListener { text, id ->
            val message: String = if (text.isNotEmpty()) "Item deleted: \"$text\"" else "Item deleted"
            Snackbar.make(main_root, message, Snackbar.LENGTH_LONG)
                .setAction("undo") {
                    main_checklist.restoreDeletedItem(id)
                }.show()
        }

        handleSettingChecklistConfiguration()
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

        // Apply item setting from config to checklist system
        checklistConfiguration.itemHorizontalPadding?.let {
            main_checklist.setItemHorizontalPadding(it)
        }
    }

    private fun dismissKeyboard() {
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }
}

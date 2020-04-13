package com.dvdb.checklist.sample

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dvdb.checklist.R
import com.dvdb.checklist.manager.getFormattedTextItems
import com.dvdb.checklist.manager.restoreDeletedItem
import com.dvdb.checklist.manager.setItems
import com.dvdb.checklist.manager.setOnItemDeletedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val CHECKLIST_ITEMS_TEXT = "[ ] Send meeting notes to team\n" +
        "[ ] Order flowers\n" +
        "[ ] Organise vacation photos\n" +
        "[ ] Book holiday flights\n" +
        "[ ] Scan vaccination certificates\n" +
        "[x] Advertise holiday home\n" +
        "[x] Wish Sarah happy birthday"

private const val SP_CHECKLIST_ITEMS_TEXT_KEY = "cl_items_text"

class MainActivity : AppCompatActivity() {

    private var checklistItemsText: String = CHECKLIST_ITEMS_TEXT
        get() {
            val sp: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
            return sp.getString(SP_CHECKLIST_ITEMS_TEXT_KEY, field) ?: field
        }
        set(value) {
            field = value
            val sp: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
            sp.edit().putString(SP_CHECKLIST_ITEMS_TEXT_KEY, value).apply()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initChecklist()
    }

    private fun initChecklist() {
        main_checklist.setItems(checklistItemsText)

        main_checklist.setOnItemDeletedListener { text, id ->
            Snackbar.make(main_root, "Item deleted: \"$text\"", Snackbar.LENGTH_LONG)
                .setAction("undo") {
                    main_checklist.restoreDeletedItem(id)
                }.show()
        }
    }

    override fun onStop() {
        checklistItemsText = main_checklist.getFormattedTextItems()
        super.onStop()
    }
}

package com.dvdb.checklist.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dvdb.checklist.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_checklist.setItems(
            "[ ] Send meeting notes to team\n" +
                    "[ ] Order flowers\n" +
                    "[ ] Organise vacation photos\n" +
                    "[ ] Book holiday flights\n" +
                    "[ ] Scan vaccination certificates\n" +
                    "[x] Advertise holiday home\n" +
                    "[x] Wish Sarah happy birthday"
        )
    }
}

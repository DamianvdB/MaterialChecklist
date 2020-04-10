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
            "[ ] Slack meeting notes to team\n" +
                    "[ ] Order flowers for girlfriend\n" +
                    "[ ] Organise vacation photos\n" +
                    "[ ] Book flights to Dubai\n" +
                    "[x] Airbnb holiday home"
        )
    }
}

package com.dvdb.checklist.util

import android.os.Handler

private const val DEFAULT_DELAY_MS = 100L

internal class DelayHandler {
    private val handler: Handler = Handler()

    fun run(
        delayMs: Long = DEFAULT_DELAY_MS,
        runnable: () -> Unit
    ) {
        handler.postDelayed(
            {
                runnable()
            },
            delayMs
        )
    }
}
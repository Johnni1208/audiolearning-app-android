package com.audiolearning.app.extension

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Converts Long-Timestamp ([System.currentTimeMillis]) to date formatted like this: dd.mm.yy.
 */
fun Long.toFormattedDate(): String {
    val formatter: SimpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }

    return formatter.format(Date(this))
}

/**
 * Converts Long-milliseconds to format: mm:ss.
 */
fun Long.toTimeString() =
    String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(this),
        TimeUnit.MILLISECONDS.toSeconds(this) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    )

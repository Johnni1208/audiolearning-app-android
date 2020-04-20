package com.audiolearning.app.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts Long-Timestamp to date formatted like this: dd.mm.yy.
 */
fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }

    return formatter.format(Date(this))
}
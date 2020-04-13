package com.audiolearning.app.extensions

import java.text.DateFormat.getDateTimeInstance
import java.util.*

/**
 * Converts Long-Timestamp to date formatted like this: dd.mm.yy.
 */
fun Long.toFormattedDate(): String = getDateTimeInstance().format(Date(this))
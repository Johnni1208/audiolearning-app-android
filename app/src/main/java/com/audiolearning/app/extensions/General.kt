package com.audiolearning.app.extensions

fun getTimeStringFromMillis(millis: Long): String {
    val minutes = millis / 1000 / 60 % 60
    val seconds = millis / 1000 % 60

    return "${if (minutes > 9) "$minutes" else "0$minutes"}:" +
            if (seconds > 9) "$seconds" else "0$seconds"
}
package com.audiolearning.app.extensions

import java.util.concurrent.TimeUnit

fun getTimeStringFromMillis(millis: Long) =
    String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millis),
        TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
    )

package com.audiolearning.app.extension

import android.content.res.Resources

fun Float.toDp() = (this * Resources.getSystem().displayMetrics.density)

package com.audiolearning.app.extension

import android.content.res.Resources

fun Float.dp() = (this * Resources.getSystem().displayMetrics.density)

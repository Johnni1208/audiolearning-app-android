package com.audiolearning.app.util

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.audiolearning.app.R

class ColorHelper(context: Context) {
    val ripple: Int = TypedValue().apply {
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
    }.resourceId

    val yellow50: Int = ContextCompat.getColor(
        context,
        R.color.yellow_50
    )

    val yellow600: Int = ContextCompat.getColor(
        context,
        R.color.yellow_600
    )

    val yellow700: Int = ContextCompat.getColor(
        context,
        R.color.yellow_700
    )

    val white: Int = ContextCompat.getColor(
        context,
        android.R.color.white
    )

    val colorDivider: Int = ContextCompat.getColor(
        context,
        R.color.colorDivider
    )

    val colorTextPrimary: Int = ContextCompat.getColor(
        context,
        R.color.colorTextPrimary
    )

    val colorTextSecondary: Int = ContextCompat.getColor(
        context,
        R.color.colorTextSecondary
    )
}

package com.audiolearning.app.extensions

import android.view.View
import android.view.Window
import android.view.WindowManager

/**
 * Shows the SoftKeyboard.
 *
 * @param focusable [View.requestFocus] for the given view
 */
fun Window.showKeyboard(focusable: View? = null) {
    focusable?.requestFocus()
    this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}

/**
 * Hides the SoftKeyboard.
 */
fun Window.hideKeyboard() {
    this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}
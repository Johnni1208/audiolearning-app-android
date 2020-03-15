package com.audiolearning.app.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Sets the visibility of the view to [View.VISIBLE]
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * Sets the visibility of the view to [View.GONE]
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * Shows the SoftKeyboard on View.
 */
fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

/**
 * Hides SoftKeyboard on View.
 */
fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}
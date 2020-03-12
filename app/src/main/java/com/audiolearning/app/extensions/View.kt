package com.audiolearning.app.extensions

import android.view.View

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
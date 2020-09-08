package com.audiolearning.app.extension

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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

fun View.fadeIn() {
    if (this.visibility == View.VISIBLE) return

    this.apply {
        show()
        alpha = 0f

        animate()
            .alpha(1f)
            .setListener(null)
    }
}

fun View.fadeOut(finalVisibility: Int = View.GONE) {
    if (this.visibility == View.GONE || this.visibility == View.INVISIBLE) return

    this.animate()
        .alpha(0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@fadeOut.visibility = finalVisibility
            }
        })
}

fun View.animateAlphaTo(value: Float) {
    if (this.alpha == value) return

    this.animate()
        .setDuration(resources.getInteger(android.R.integer.config_shortAnimTime).toLong())
        .alpha(value)
}

package com.audiolearning.app.ui.components

import android.content.Context
import android.util.AttributeSet
import android.widget.Spinner

/**
 * Custom Spinner, which triggers a second time when an item is selected a second time.
 */
class DoubleSelectSpinner(context: Context, attributeSet: AttributeSet) :
    Spinner(context, attributeSet) {
    override fun setSelection(position: Int) {
        val sameSelected: Boolean = position == selectedItemPosition
        super.setSelection(position)

        if (sameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }
}
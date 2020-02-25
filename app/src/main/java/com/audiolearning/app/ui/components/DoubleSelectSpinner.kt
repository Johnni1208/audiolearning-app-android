package com.audiolearning.app.ui.components

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

/**
 * Custom Spinner, which triggers a second time when an item is selected a second time.
 */
class DoubleSelectSpinner(context: Context, attributeSet: AttributeSet) :
    AppCompatSpinner(context, attributeSet) {
    override fun setSelection(position: Int) {
        val sameSelected: Boolean = position == selectedItemPosition
        super.setSelection(position)

        if (sameSelected) {
            onItemSelectedListener?.onItemSelected(this, selectedView, position, selectedItemId)
        }
    }
}
package com.audiolearning.app.extensions

import android.view.View
import android.view.Window
import android.view.WindowManager
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class WindowTest {
    private val mockWindow: Window = mock()
    private val view: View = mock()

    @Test
    fun showKeyboard_ShouldRequestFocusOnGivenView() {
        mockWindow.showKeyboard(view)

        verify(view).requestFocus()
    }

    @Test
    fun showKeyboard_ShouldSetSoftInputModeToVisible() {
        mockWindow.showKeyboard(view)

        verify(mockWindow).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    @Test
    fun hideKeyboard_ShouldSetSoftInputModeToHidden() {
        mockWindow.hideKeyboard()

        verify(mockWindow).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }
}
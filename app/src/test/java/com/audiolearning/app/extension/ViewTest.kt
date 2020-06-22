package com.audiolearning.app.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times

class ViewTest {
    private val mockView: View = mock()
    private val mockInputMethodManager: InputMethodManager = mock()


    @Before
    fun setupContext() {
        whenever(mockView.context).thenReturn(mock(Context::class.java))
        whenever(mockView.context.getSystemService(Context.INPUT_METHOD_SERVICE)).thenReturn(
            mockInputMethodManager
        )
    }

    @Test
    fun show_ShouldSetVisibilityToVisible() {
        mockView.show()

        verify(mockView).visibility = View.VISIBLE
    }

    @Test
    fun hide_ShouldSetVisibilityToGone() {
        mockView.hide()

        verify(mockView).visibility = View.GONE
    }

    @Test
    fun showKeyboard_ShouldCallToShowKeyboard() {
        mockView.showKeyboard()

        verify(mockView, times(1)).requestFocus()
        verify(mockInputMethodManager, times(1)).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @Test
    fun hideKeyboard_ShouldCallToHideKeyboard() {
        mockView.hideKeyboard()

        verify(
            mockInputMethodManager,
            times(1)
        ).toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }
}
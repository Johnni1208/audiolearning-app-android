package com.audiolearning.app.extension

import androidx.fragment.app.Fragment
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FragmentTest {
    private class TestMethods {
        fun test() {}
    }

    private lateinit var testMethodsImpl: TestMethods

    @Before
    fun setup() {
        testMethodsImpl = mock()
    }

    @Test
    fun runOnUiThread_ShouldReturnIfFragmentIsNull() {
        val nullFragment: Fragment? = null

        nullFragment.runOnUiThread {
            testMethodsImpl.test()
        }

        verify(testMethodsImpl, times(0)).test()
    }

    @Test
    fun runOnUiThread_ShouldReturnIfFragmentIsNotAdded() {
        val nullFragment: Fragment = mock()
        whenever(nullFragment.isAdded).thenReturn(false)

        nullFragment.runOnUiThread {
            testMethodsImpl.test()
        }

        verify(testMethodsImpl, times(0)).test()
    }
}

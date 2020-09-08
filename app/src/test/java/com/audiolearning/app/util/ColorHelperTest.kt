package com.audiolearning.app.util

import android.content.Context
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ColorHelperTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val colorHelper = ColorHelper(context)

    @Test
    fun ripple_ShouldReturnRippleResource() {
        val rippleResource = TypedValue().apply {
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, this, true)
        }.resourceId

        assertEquals(rippleResource, colorHelper.ripple)
    }

    @Test
    fun yellow50_ShouldReturnColorYellow50() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.yellow_50
        )

        assertEquals(expectedColor, colorHelper.yellow50)
    }

    @Test
    fun yellow600_ShouldReturnColorYellow600() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.yellow_600
        )

        assertEquals(expectedColor, colorHelper.yellow600)
    }

    @Test
    fun yellow700_ShouldReturnColorYellow700() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.yellow_700
        )

        assertEquals(expectedColor, colorHelper.yellow700)
    }

    @Test
    fun white_ShouldReturnColorWhite() {
        val expectedColor = ContextCompat.getColor(
            context,
            android.R.color.white
        )

        assertEquals(expectedColor, colorHelper.white)
    }

    @Test
    fun colorDivider_ShouldReturnColorDivider() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.colorDivider
        )

        assertEquals(expectedColor, colorHelper.colorDivider)
    }

    @Test
    fun colorTextPrimary_ShouldReturnColorTextPrimary() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.colorTextPrimary
        )

        assertEquals(expectedColor, colorHelper.colorTextPrimary)
    }

    @Test
    fun colorTextSecondary_ShouldReturnColorTextSecondary() {
        val expectedColor = ContextCompat.getColor(
            context,
            R.color.colorTextSecondary
        )

        assertEquals(expectedColor, colorHelper.colorTextSecondary)
    }
}

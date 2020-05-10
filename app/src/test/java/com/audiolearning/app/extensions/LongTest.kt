package com.audiolearning.app.extensions

import org.junit.Assert
import org.junit.Test
import java.util.regex.Pattern

class LongTest {
    @Test
    fun toTimeString_ShouldReturnTheRightFormat() {
        // 00:00
        val expectedFormat = "\\d\\d:\\d\\d"

        Assert.assertTrue(Pattern.matches(expectedFormat, 1000L.toTimeString()))
    }

    @Test
    fun toFormattedDate_ShouldReturnTheRightFormat() {
        // dd.MM.yyyy
        val expectedFormat = "\\d\\d.\\d\\d.\\d\\d\\d\\d"

        Assert.assertTrue(
            Pattern.matches(
                expectedFormat,
                System.currentTimeMillis().toFormattedDate()
            )
        )
    }
}
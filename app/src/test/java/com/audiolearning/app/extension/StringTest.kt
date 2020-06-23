package com.audiolearning.app.extension

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringTest {
    @Test
    fun isAllowedFileName_ShouldReturnTrue_ifNameIsAllowed() {
        val allowedName = "test"

        assertTrue(allowedName.isAllowedFileName())
    }

    @Test
    fun isAllowedFileName_ShouldReturnFalse_ifNameIsNotAllowed() {
        val notAllowedName = "/../Test.mp3"

        assertFalse(notAllowedName.isAllowedFileName())
    }
}

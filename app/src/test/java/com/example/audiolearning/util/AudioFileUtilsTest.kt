package com.example.audiolearning.util

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioFileUtilsTest {
    @Test
    fun isFileNameAllowed_ShouldReturnTrue_ifNameIsAllowed() {
        val allowedName = "test"

        assertTrue(AudioFileUtils.isFileNameAllowed(allowedName))
    }

    @Test
    fun isFileNameAllowed_ShouldReturnFalse_ifNameIsNotAllowed() {
        val notAllowedName = "/../Test.mp3"

        assertFalse(AudioFileUtils.isFileNameAllowed(notAllowedName))
    }
}
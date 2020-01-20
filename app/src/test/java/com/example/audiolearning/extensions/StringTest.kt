package com.example.audiolearning.extensions

import org.junit.Assert
import org.junit.Test

class StringTest {
    @Test
    fun isAllowedFileName_ShouldReturnTrue_ifNameIsAllowed() {
        val allowedName = "test"

        Assert.assertTrue(allowedName.isAllowedFileName())
    }

    @Test
    fun isAllowedFileName_ShouldReturnFalse_ifNameIsNotAllowed() {
        val notAllowedName = "/../Test.mp3"

        Assert.assertFalse(notAllowedName.isAllowedFileName())
    }
}
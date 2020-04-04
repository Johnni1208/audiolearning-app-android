package com.audiolearning.app.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ArrayListTest {
    private val itemString = "item"
    @Test
    fun addIfNotContained_ShouldReturnFalse_IfItemIsInList() {
        val arrayList = arrayListOf(itemString, "test")

        assertFalse(arrayList.addIfNotContained(itemString))
    }

    @Test
    fun addIfNotContained_ShouldReturnTrue_IfItemIsNotInList() {
        val arrayList = arrayListOf("test")

        assertTrue(arrayList.addIfNotContained(itemString))
    }

    @Test
    fun addIfNotContained_ShouldAddItemToTheList_IfItemIsNotInList() {
        val arrayList = arrayListOf("test")

        arrayList.addIfNotContained(itemString)

        assertTrue(arrayList.contains(itemString))
    }

    @Test
    fun removeIfContained_ShouldReturnFalse_IfItemIsNotInList() {
        val arrayList = arrayListOf("test")

        assertFalse(arrayList.removeIfContained(itemString))
    }

    @Test
    fun removeIfContained_ShouldReturnFalse_IfItemIsInList() {
        val arrayList = arrayListOf(itemString, "test")

        assertTrue(arrayList.removeIfContained(itemString))
    }

    @Test
    fun removeIfContained_ShouldRemoveItemFromTheList_IfItemIsInList() {
        val arrayList = arrayListOf(itemString, "test")

        arrayList.removeIfContained(itemString)

        assertFalse(arrayList.contains(itemString))
    }
}
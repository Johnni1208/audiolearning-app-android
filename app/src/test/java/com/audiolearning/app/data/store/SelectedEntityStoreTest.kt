package com.audiolearning.app.data.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.audiolearning.app.data.db.entities.BaseEntity
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SelectedEntityStoreTest {
    @get:Rule
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private val testEntity = object : BaseEntity {
        override var id: Int? = 0
    }

    private lateinit var selectedEntityStore: SelectedEntityStore<BaseEntity>

    @Before
    fun setupStore() {
        selectedEntityStore = SelectedEntityStore()
    }

    @Test
    fun select_ShouldAddItemToSelectedList() {
        Assert.assertTrue(selectedEntityStore.select(testEntity))
        Assert.assertEquals(testEntity, selectedEntityStore.selectedEntityList.value?.first())
    }

    @Test
    fun select_ShouldNotAddItemToListIfItIsAlreadyInIt() {
        selectedEntityStore.select(testEntity)

        Assert.assertFalse(selectedEntityStore.selectedEntityList.value?.size!! > 1)
        Assert.assertFalse(selectedEntityStore.select(testEntity))
    }

    @Test
    fun deselectSubjectItem_ShouldRemoveEntityFromSelectedList() {
        selectedEntityStore.select(testEntity)

        Assert.assertTrue(selectedEntityStore.deselect(testEntity))
        Assert.assertFalse(selectedEntityStore.selectedEntityList.value?.contains(testEntity)!!)
    }

    @Test
    fun deselectSubjectItem_ShouldNotRemoveEntityIfItIsNotInTheList() {
        val secondTestEntity = object : BaseEntity {
            override var id: Int? = 1
        }

        selectedEntityStore.select(secondTestEntity)

        Assert.assertFalse(selectedEntityStore.deselect(testEntity))
        Assert.assertTrue(selectedEntityStore.selectedEntityList.value?.size == 1)
    }

    @Test
    fun clear_ShouldRemoveAllEntitiesFromTheList() {
        selectedEntityStore.select(testEntity)
        selectedEntityStore.clear()

        Assert.assertTrue(selectedEntityStore.selectedEntityList.value?.size == 0)
    }
}
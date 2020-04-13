package com.audiolearning.app.adapters

import android.view.View
import android.view.ViewGroup
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.BaseEntity
import com.audiolearning.app.data.db.entities.Subject
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BaseSelectableRecyclerViewAdapterTest {
    private val mockData: ArrayList<BaseEntity> = mock()

    private inner class TestBaseSelectableRecyclerViewAdapterClass :
        BaseSelectableRecyclerViewAdapter() {
        init {
            data = mockData
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BaseSelectableViewHolder =
            object :
                BaseSelectableViewHolder(
                    mock(View::class.java),
                    mock(ItemSelectListener::class.java)
                ) {
                override fun setViewSelectedUi() {}

                override fun setViewDeselectUi() {}
            }


        override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {}

    }

    private val realData = ArrayList<Subject>().apply {
        add(Subject("test1", ""))
        add(Subject("test2", ""))
        add(Subject("test3", ""))
        add(Subject("test4", ""))
    }
    private lateinit var baseSelectableRecyclerViewAdapter: TestBaseSelectableRecyclerViewAdapterClass

    @Before
    fun setup() {
        baseSelectableRecyclerViewAdapter = TestBaseSelectableRecyclerViewAdapterClass()
    }

    @Test
    fun getItemCount_ShouldReturnSizeOfData() {
        val baseSelectableRecyclerViewAdapterWithRealData =
            TestBaseSelectableRecyclerViewAdapterClass().apply {
                initializeData(realData)
            }
        assertEquals(realData.size, baseSelectableRecyclerViewAdapterWithRealData.itemCount)
    }

    @Test
    fun getItemViewType_ShouldReturnArgument() {
        val argument = 1
        assertEquals(1, baseSelectableRecyclerViewAdapter.getItemViewType(argument))
    }

    @Test(expected = IllegalStateException::class)
    fun initializeData_ShouldThrowError_WhenIsDataInitializedIsTrue() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true

        baseSelectableRecyclerViewAdapter.initializeData(mockData)
    }

    @Test
    fun initializeData_ShouldSetIsDataInitializedToTrue() {
        baseSelectableRecyclerViewAdapter.initializeData(realData)

        assertEquals(true, baseSelectableRecyclerViewAdapter.isDataInitialized)
    }

    @Test(expected = IllegalStateException::class)
    fun updateData_ShouldThrowError_WhenIsDataInitializedIsFalse() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = false

        baseSelectableRecyclerViewAdapter.updateData(mockData)
    }

    @Test
    fun updateData_ShouldAddItemToDataIfNewListSizeIsBigger() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(1)

        baseSelectableRecyclerViewAdapter.updateData(realData)

        verify(mockData, Mockito.times(1)).add(realData.last())
    }

    @Test
    fun updateData_ShouldReturnITEMS_ADDEDIfNewListSizeIsBigger() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(0)

        val event = baseSelectableRecyclerViewAdapter.updateData(realData)

        assertEquals(AdapterDataEvent.ITEMS_ADDED, event)
    }

    @Test
    fun updateData_ShouldDeleteItemsIfNewListSizeIsSmaller() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)
        whenever(mockData.contains(ArgumentMatchers.any(Subject::class.java))).thenReturn(false)

        baseSelectableRecyclerViewAdapter.updateData(realData)

        verify(
            mockData,
            Mockito.times(realData.size + 1)
        ).removeAt(ArgumentMatchers.any(Int::class.java))
    }

    @Test
    fun updateDate_ShouldOnlyDeleteItemsWhichAreNotPresentInTheNewData() {
        val position = 0
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)
        // Item at position is in the data of the adapter
        whenever(mockData[position]).thenReturn(realData[position])

        baseSelectableRecyclerViewAdapter.updateData(realData)

        // So it should not be deleted
        verify(mockData, Mockito.never()).removeAt(position)
    }

    @Test
    fun updateDate_ShouldOnlyDeleteItemsInReversedOrder() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)

        whenever(mockData[0]).thenReturn(realData[0])
        whenever(mockData[1]).thenReturn(realData[1])

        baseSelectableRecyclerViewAdapter.updateData(realData)

        // positions to be deleted: 2, 3
        val inOrder = Mockito.inOrder(mockData)
        inOrder.verify(mockData, Mockito.times(1)).removeAt(3)
        inOrder.verify(mockData, Mockito.times(1)).removeAt(2)
    }

    @Test
    fun updateData_ShouldReturnITEMS_DELETEDIfNewListSizeIsSmaller() {
        baseSelectableRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)

        val event = baseSelectableRecyclerViewAdapter.updateData(realData)

        assertEquals(AdapterDataEvent.ITEMS_DELETED, event)
    }
}
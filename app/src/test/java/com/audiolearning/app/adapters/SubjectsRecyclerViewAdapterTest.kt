package com.audiolearning.app.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.audiolearning.app.adapters.subjects_recycler_view.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapters.subjects_recycler_view.SubjectsRecyclerViewAdapterEvent
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extensions.hide
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.subject_cardview.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectsRecyclerViewAdapterTest {
    private val realData = ArrayList<Subject>().apply {
        add(Subject("test1", ""))
        add(Subject("test2", ""))
        add(Subject("test3", ""))
        add(Subject("test4", ""))
    }
    private val mockData: ArrayList<Subject> = mock()
    private val mockSubjectEventListener: SubjectsRecyclerViewAdapter.SubjectEventListener = mock()
    private lateinit var subjectsRecyclerViewAdapter: SubjectsRecyclerViewAdapter

    @Before
    fun setup() {
        subjectsRecyclerViewAdapter =
            SubjectsRecyclerViewAdapter(mockData, mockSubjectEventListener)
    }

    @Test
    fun onBindViewHolder_ShouldApplyNormalStyleToViewHolder() {
        val position = 0
        val subjectsRecyclerViewAdapterWithRealData =
            SubjectsRecyclerViewAdapter(realData, mockSubjectEventListener)

        val subjectViewHolder: SubjectsRecyclerViewAdapter.SubjectViewHolder = mock()
        whenever(subjectViewHolder.subjectCardView).thenReturn(mock(CardView::class.java))
        whenever(subjectViewHolder.subjectCardView.tv_subject_name).thenReturn(mock(TextView::class.java))
        whenever(subjectViewHolder.subjectCardView.iv_check_circle).thenReturn(mock(ImageView::class.java))

        subjectsRecyclerViewAdapterWithRealData.onBindViewHolder(subjectViewHolder, position)

        verify(subjectViewHolder.subjectCardView.tv_subject_name, times(1)).text =
            realData[position].name
        verify(subjectViewHolder.subjectCardView, times(1)).alpha = 1f
        verify(subjectViewHolder.subjectCardView.iv_check_circle, times(1)).hide()
    }

    @Test
    fun getItemCount_ShouldReturnSizeOfData() {
        val subjectsRecyclerViewAdapterWithRealData =
            SubjectsRecyclerViewAdapter(realData, mockSubjectEventListener)
        assertEquals(realData.size, subjectsRecyclerViewAdapterWithRealData.itemCount)
    }

    @Test
    fun getItemViewType_ShouldReturnArgument() {
        val argument = 1
        assertEquals(1, subjectsRecyclerViewAdapter.getItemViewType(argument))
    }

    @Test(expected = IllegalStateException::class)
    fun setInitialData_ShouldThrowError_WhenIsDataInitializedIsTrue() {
        subjectsRecyclerViewAdapter.isDataInitialized = true

        subjectsRecyclerViewAdapter.setInitialData(mockData)
    }

    @Test
    fun setInitialData_ShouldSetIsDataInitializedToTrue() {
        subjectsRecyclerViewAdapter.setInitialData(realData)

        assertEquals(true, subjectsRecyclerViewAdapter.isDataInitialized)
    }

    @Test(expected = IllegalStateException::class)
    fun updateData_ShouldThrowError_WhenIsDataInitializedIsFalse() {
        subjectsRecyclerViewAdapter.isDataInitialized = false

        subjectsRecyclerViewAdapter.updateData(mockData)
    }

    @Test
    fun updateData_ShouldAddItemToDataIfNewListSizeIsBigger() {
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(1)

        subjectsRecyclerViewAdapter.updateData(realData)

        verify(mockData, times(1)).add(realData.last())
    }

    @Test
    fun updateData_ShouldReturnITEMS_ADDEDIfNewListSizeIsBigger() {
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(0)

        val event = subjectsRecyclerViewAdapter.updateData(realData)

        assertEquals(SubjectsRecyclerViewAdapterEvent.ITEMS_ADDED, event)
    }

    @Test
    fun updateData_ShouldDeleteItemsIfNewListSizeIsSmaller() {
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)
        whenever(mockData.contains(ArgumentMatchers.any(Subject::class.java))).thenReturn(false)

        subjectsRecyclerViewAdapter.updateData(realData)

        verify(mockData, times(realData.size + 1)).removeAt(ArgumentMatchers.any(Int::class.java))
    }

    @Test
    fun updateDate_ShouldOnlyDeleteItemsWhichAreNotPresentInTheNewData() {
        val position = 0
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)
        // Item at position is in the data of the adapter
        whenever(mockData[position]).thenReturn(realData[position])

        subjectsRecyclerViewAdapter.updateData(realData)

        // So it should not be deleted
        verify(mockData, never()).removeAt(position)
    }

    @Test
    fun updateDate_ShouldOnlyDeleteItemsInReversedOrder() {
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)

        whenever(mockData[0]).thenReturn(realData[0])
        whenever(mockData[1]).thenReturn(realData[1])

        subjectsRecyclerViewAdapter.updateData(realData)

        // positions to be deleted: 2, 3
        val inOrder = inOrder(mockData)
        inOrder.verify(mockData, times(1)).removeAt(3)
        inOrder.verify(mockData, times(1)).removeAt(2)
    }

    @Test
    fun updateData_ShouldReturnITEMS_DELETEDIfNewListSizeIsSmaller() {
        subjectsRecyclerViewAdapter.isDataInitialized = true
        whenever(mockData.size).thenReturn(realData.size + 1)

        val event = subjectsRecyclerViewAdapter.updateData(realData)

        assertEquals(SubjectsRecyclerViewAdapterEvent.ITEMS_DELETED, event)
    }
}
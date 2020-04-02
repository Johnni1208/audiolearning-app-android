package com.audiolearning.app.adapters

import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.audiolearning.app.adapters.subjects_recycler_view.SubjectsRecyclerViewAdapter
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extensions.hide
import com.audiolearning.app.extensions.show
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.subject_cardview.view.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock

class SubjectViewHolderTest {
    private val mockSubjectsCardView: CardView = mock()
    private val listener: SubjectsRecyclerViewAdapter.SubjectEventListener = mock()
    private val mockData: ArrayList<Subject> = mock()
    private val testSubject = Subject("test", "").apply { id = 0 }
    private val subjectsRecyclerViewAdapter = SubjectsRecyclerViewAdapter(mockData, listener)

    @Before
    fun setup() {
        whenever(mockSubjectsCardView.iv_check_circle).thenReturn(mock(ImageView::class.java))
    }

    @Test
    fun init_ShouldSetOnClickAndOnLongClickListener() {
        val subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, listener)

        verify(mockSubjectsCardView, times(1)).setOnClickListener(subjectsViewHolder)
        verify(mockSubjectsCardView, times(1)).setOnLongClickListener(subjectsViewHolder)
    }

    @Test
    fun onClick_ShouldCallListenersSubjectItemClick_WhenIsSelectingIsFalse() {
        val mockView: View = mock()
        whenever(mockData[anyInt()]).thenReturn(testSubject)
        val subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, listener)

        subjectsRecyclerViewAdapter.isSelecting = false
        subjectsViewHolder.onClick(mockView)

        verify(listener, times(1)).onSubjectItemClick(testSubject.id!!.toInt())
    }

    @Test
    fun onClick_ShouldSelectSubjectCardView_WhenIsSelectingIsTrue() {
        val mockView: View = mock()
        whenever(mockData[anyInt()]).thenReturn(testSubject)
        val subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, listener)

        subjectsRecyclerViewAdapter.isSelecting = true
        subjectsViewHolder.onClick(mockView)

        // Selected State
        verify(mockSubjectsCardView).isSelected = true
        verify(mockSubjectsCardView.iv_check_circle).show()
        verify(mockSubjectsCardView).alpha = 0.75f

        // Listener call
        verify(listener).onSubjectItemSelect(testSubject.id!!.toInt())
    }

    @Test
    fun onClick_ShouldDeselectSubjectCardView_WhenCardIsSelected() {
        val mockView: View = mock()
        whenever(mockData[anyInt()]).thenReturn(testSubject)
        whenever(mockSubjectsCardView.isSelected).thenReturn(true)
        val subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, listener)

        subjectsRecyclerViewAdapter.isSelecting = true
        subjectsViewHolder.onClick(mockView)

        // Selected State
        verify(mockSubjectsCardView).isSelected = false
        verify(mockSubjectsCardView.iv_check_circle).hide()
        verify(mockSubjectsCardView).alpha = 1f

        // Listener call
        verify(listener).onSubjectItemDeselect(testSubject.id!!.toInt())
    }
}
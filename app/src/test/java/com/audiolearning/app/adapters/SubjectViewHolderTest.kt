package com.audiolearning.app.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
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
    private val mockListener: SubjectsRecyclerViewAdapter.SubjectEventListener = mock()
    private val mockData: ArrayList<Subject> = mock()
    private val mockView: View = mock()
    private val testSubject = Subject("test", "").apply { id = 0 }
    private val subjectsRecyclerViewAdapter = SubjectsRecyclerViewAdapter(mockData, mockListener)
    private lateinit var subjectsViewHolder: SubjectsRecyclerViewAdapter.SubjectViewHolder

    @Before
    fun setup() {
        subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, mockListener)
        whenever(mockSubjectsCardView.iv_check_circle).thenReturn(mock(ImageView::class.java))
        whenever(mockSubjectsCardView.tv_subject_name).thenReturn(mock(TextView::class.java))
        whenever(mockData[anyInt()]).thenReturn(testSubject)
    }

    @Test
    fun init_ShouldSetOnClickAndOnLongClickListener() {
        val initializedSubjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, mockListener)

        verify(mockSubjectsCardView, times(1)).setOnClickListener(initializedSubjectsViewHolder)
        verify(mockSubjectsCardView, times(1)).setOnLongClickListener(initializedSubjectsViewHolder)
    }

    @Test
    fun onClick_ShouldCallListenersSubjectItemClick_WhenIsSelectingIsFalse() {
        subjectsRecyclerViewAdapter.isSelecting = false
        subjectsViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onSubjectItemClick(
            testSubject.id!!.toInt(),
            mockSubjectsCardView.tv_subject_name
        )
    }

    @Test
    fun onClick_ShouldSelectSubjectCardView_WhenIsSelectingIsTrue() {
        subjectsRecyclerViewAdapter.isSelecting = true
        subjectsViewHolder.onClick(mockView)

        // Selected State
        verify(mockSubjectsCardView).isSelected = true
        verify(mockSubjectsCardView.iv_check_circle).show()
        verify(mockSubjectsCardView).alpha = 0.75f

        // Listener call
        verify(mockListener).onSubjectItemSelect(testSubject.id!!.toInt())
    }

    @Test
    fun onClick_ShouldDeselectSubjectCardView_WhenCardIsSelected() {
        whenever(mockSubjectsCardView.isSelected).thenReturn(true)
        val subjectsViewHolder =
            subjectsRecyclerViewAdapter.SubjectViewHolder(mockSubjectsCardView, mockListener)

        subjectsRecyclerViewAdapter.isSelecting = true
        subjectsViewHolder.onClick(mockView)

        // Selected State
        verify(mockSubjectsCardView).isSelected = false
        verify(mockSubjectsCardView.iv_check_circle).hide()
        verify(mockSubjectsCardView).alpha = 1f

        // Listener call
        verify(mockListener).onSubjectItemDeselect(testSubject.id!!.toInt())
    }
}
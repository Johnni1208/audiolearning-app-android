package com.audiolearning.app.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.audiolearning.app.adapters.recycler_view_adapter.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extensions.hide
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.subject_cardview.view.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectsRecyclerViewAdapterTest {
    private val realData = ArrayList<Subject>().apply {
        add(Subject("test1", ""))
        add(Subject("test2", ""))
        add(Subject("test3", ""))
        add(Subject("test4", ""))
    }
    private val mockSubjectEventListener: ItemSelectListener = mock()

    @Test
    fun onBindViewHolder_ShouldApplyNormalStyleToViewHolder() {
        val position = 0
        val subjectsRecyclerViewAdapterWithRealData =
            SubjectsRecyclerViewAdapter(mockSubjectEventListener).apply {
                initializeData(realData)
            }

        val mockCardView: CardView = mock()
        val subjectViewHolder: SubjectsRecyclerViewAdapter.SubjectViewHolder =
            subjectsRecyclerViewAdapterWithRealData.SubjectViewHolder(
                mockCardView,
                mockSubjectEventListener
            )
        whenever(subjectViewHolder.itemView.tv_subject_name).thenReturn(mock(TextView::class.java))
        whenever(subjectViewHolder.itemView.iv_check_circle).thenReturn(mock(ImageView::class.java))

        subjectsRecyclerViewAdapterWithRealData.onBindViewHolder(subjectViewHolder, position)

        verify(subjectViewHolder.itemView.tv_subject_name, times(1)).text =
            realData[position].name
        verify(subjectViewHolder.itemView, times(1)).alpha = 1.0f
        verify(subjectViewHolder.itemView.iv_check_circle, times(1)).hide()
    }
}
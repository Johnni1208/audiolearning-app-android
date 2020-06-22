package com.audiolearning.app.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.audiolearning.app.adapter.recycler.selectable.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.extension.hide
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.subject_cardview.view.iv_check_circle
import kotlinx.android.synthetic.main.subject_cardview.view.tv_subject_name
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
    private val mockSubjectEventListener: ItemSelectListener<Subject> = mock()

    @Test
    fun onBindViewHolder_ShouldApplyNormalStyleToViewHolder() {
        val position = 0
        val subjectsRecyclerViewAdapterWithRealData =
            SubjectsRecyclerViewAdapter(
                mockSubjectEventListener
            ).apply {
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

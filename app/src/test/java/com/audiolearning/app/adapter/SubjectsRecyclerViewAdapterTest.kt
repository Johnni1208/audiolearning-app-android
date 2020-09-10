package com.audiolearning.app.adapter

import android.content.Context
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.adapter.recycler.selectable.SubjectsRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.Subject
import com.audiolearning.app.util.ColorHelper
import com.google.android.material.card.MaterialCardView
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.subject_item.view.tv_subject_name
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectsRecyclerViewAdapterTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val realData = ArrayList<Subject>().apply {
        add(Subject("test1", ""))
        add(Subject("test2", ""))
        add(Subject("test3", ""))
        add(Subject("test4", ""))
    }
    private val colorHelper = ColorHelper(context)
    private val mockSubjectEventListener: ItemSelectListener<Subject> = mock()
    private val mockCardView: MaterialCardView = mock()

    private lateinit var subjectsRecyclerViewAdapterWithRealData: SubjectsRecyclerViewAdapter

    private lateinit var subjectViewHolder: SubjectsRecyclerViewAdapter.SubjectViewHolder

    @Before
    fun setup() {
        whenever(mockCardView.context).thenReturn(context)

        subjectsRecyclerViewAdapterWithRealData =
            SubjectsRecyclerViewAdapter(
                mockSubjectEventListener
            ).apply {
                initializeData(realData)
            }

        subjectViewHolder = subjectsRecyclerViewAdapterWithRealData.SubjectViewHolder(
            mockCardView,
            mockSubjectEventListener
        ).apply {
            whenever(this.itemView.tv_subject_name).thenReturn(mock(TextView::class.java))
        }
    }

    @Test
    fun onBindViewHolder_ShouldApplyDeselectedStyleToViewHolder() {
        val spyViewHolder = spy(
            subjectsRecyclerViewAdapterWithRealData.SubjectViewHolder(
                mockCardView,
                mockSubjectEventListener
            )
        )

        subjectsRecyclerViewAdapterWithRealData.onBindViewHolder(spyViewHolder, 0)

        verify(spyViewHolder, times(1)).showViewDeselectedUi()
    }

    @Test
    fun showViewSelectedUi_ShouldApplyCorrectStyleToCardView() {
        subjectViewHolder.showViewSelectedUi()

        verify((subjectViewHolder.itemView as MaterialCardView), times(1)).setCardBackgroundColor(
            colorHelper.yellow50
        )
        verify((subjectViewHolder.itemView as MaterialCardView), times(1)).strokeColor =
            colorHelper.yellow700
        verify(
            subjectViewHolder.itemView.tv_subject_name,
            times(1)
        ).setTextColor(colorHelper.yellow700)
    }

    @Test
    fun showViewDeselectedUi_ShouldApplyCorrectStyleToCardView() {
        subjectViewHolder.showViewDeselectedUi()

        verify((subjectViewHolder.itemView as MaterialCardView), times(1)).setCardBackgroundColor(
            colorHelper.white
        )
        verify((subjectViewHolder.itemView as MaterialCardView), times(1)).strokeColor =
            colorHelper.colorDivider
        verify(
            subjectViewHolder.itemView.tv_subject_name,
            times(1)
        ).setTextColor(colorHelper.colorTextPrimary)
    }
}

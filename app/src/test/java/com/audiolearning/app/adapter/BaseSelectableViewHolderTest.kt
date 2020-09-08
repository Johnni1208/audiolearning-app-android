package com.audiolearning.app.adapter

import android.view.View
import android.view.ViewGroup
import com.audiolearning.app.adapter.recycler.selectable.base.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapter.recycler.selectable.base.ItemSelectListener
import com.audiolearning.app.data.db.entities.BaseEntity
import com.audiolearning.app.data.db.entities.Subject
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class BaseSelectableViewHolderTest {
    private val mockData: ArrayList<BaseEntity> = mock()
    private val mockView: View = mock()
    private val mockListener: ItemSelectListener<BaseEntity> = mock()

    private inner class TestBaseSelectableRecyclerViewAdapterClass :
        BaseSelectableRecyclerViewAdapter<BaseEntity>() {
        init {
            data = mockData
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BaseSelectableViewHolder = TestBaseSelectableViewHolder()

        override fun onBindViewHolder(holder: BaseSelectableViewHolder, position: Int) {}

        inner class TestBaseSelectableViewHolder :
            BaseSelectableViewHolder(mockView, mockListener) {
            override fun setViewSelectedUi() {}
            override fun setViewDeselectedUi() {}
        }
    }

    private lateinit var baseSelectableRecyclerViewAdapter: TestBaseSelectableRecyclerViewAdapterClass
    private lateinit var baseSelectableViewHolder: TestBaseSelectableRecyclerViewAdapterClass.TestBaseSelectableViewHolder
    private val testSubject = Subject("test", "").apply { id = 0 }

    @Before
    fun setup() {
        baseSelectableRecyclerViewAdapter = TestBaseSelectableRecyclerViewAdapterClass()
        baseSelectableViewHolder = baseSelectableRecyclerViewAdapter.TestBaseSelectableViewHolder()
        whenever(mockData[any()]).thenReturn(testSubject)
    }

    @Test
    fun init_ShouldSetOnClickAndOnLongClickListener() {
        val initializedViewHolder =
            baseSelectableRecyclerViewAdapter.TestBaseSelectableViewHolder()

        verify(mockView, times(1)).setOnClickListener(initializedViewHolder)
        verify(mockView, times(1)).setOnLongClickListener(initializedViewHolder)
    }

    @Test
    fun onClick_ShouldCallListenersSubjectItemClick_WhenIsSelectingIsFalse() {
        baseSelectableRecyclerViewAdapter.isSelecting = false
        baseSelectableViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onItemClick(testSubject)
    }

    @Test
    fun onClick_ShouldSelectItem_WhenIsSelectingIsTrueAndItemIsDeselected() {
        baseSelectableRecyclerViewAdapter.isSelecting = true
        baseSelectableViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onItemSelect(testSubject)
        verify(mockView, times(1)).isSelected = true
    }

    @Test
    fun onClick_ShouldDeselectItem_WhenIsSelectingIsTrueAndItemIsSelected() {
        baseSelectableRecyclerViewAdapter.isSelecting = true
        whenever(mockView.isSelected).thenReturn(true)
        baseSelectableViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onItemDeselect(testSubject)
        verify(mockView, times(1)).isSelected = false
    }
}

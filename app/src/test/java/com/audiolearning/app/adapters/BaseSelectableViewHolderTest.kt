package com.audiolearning.app.adapters

import android.view.View
import android.view.ViewGroup
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.BaseSelectableRecyclerViewAdapter
import com.audiolearning.app.adapters.recycler_view_adapter.base_selectable_adapter.ItemSelectListener
import com.audiolearning.app.data.db.entities.BaseEntity
import com.audiolearning.app.data.db.entities.Subject
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test

class BaseSelectableViewHolderTest {
    private val mockData: ArrayList<BaseEntity> = mock()
    private val mockView: View = mock()
    private val mockListener: ItemSelectListener = mock()

    private inner class TestBaseSelectableRecyclerViewAdapterClass :
        BaseSelectableRecyclerViewAdapter() {
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
            override fun setViewDeselectUi() {}
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

        verify(mockListener, times(1)).onItemClick(testSubject.id!!.toInt())
    }

    @Test
    fun onClick_ShouldSelectItem_WhenIsSelectingIsTrueAndItemIsDeselected() {
        baseSelectableRecyclerViewAdapter.isSelecting = true
        baseSelectableViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onItemSelect(testSubject.id!!.toInt())
        verify(mockView, times(1)).isSelected = true
    }

    @Test
    fun onClick_ShouldDeselectItem_WhenIsSelectingIsTrueAndItemIsSelected() {
        baseSelectableRecyclerViewAdapter.isSelecting = true
        whenever(mockView.isSelected).thenReturn(true)
        baseSelectableViewHolder.onClick(mockView)

        verify(mockListener, times(1)).onItemDeselect(testSubject.id!!.toInt())
        verify(mockView, times(1)).isSelected = false
    }
}
package com.audiolearning.app.adapters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.audiolearning.app.R
import com.audiolearning.app.data.db.entities.Subject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectSpinnerAdapterTest {
    private val context: Context = ApplicationProvider.getApplicationContext()
    private var testSubjectList = ArrayList<Subject>()
    private val textViewResourceId = R.layout.subject_spinner_item

    @Before
    fun setUp() {
        for (i in 1..10) {
            testSubjectList.add(Subject("testSubject$i", "").apply {
                id = i.toLong()
            })
        }
    }

    @Test
    fun createWithAddHint_ShouldAddAnAddHintAtFirstPosition() {
        val expectedAddHintItem = Subject("Add new subject…", "")

        val subjectArrayAdapter =
            SubjectSpinnerAdapter.createWithAddHint(
                context,
                testSubjectList,
                false
            )

        assertEquals(expectedAddHintItem, subjectArrayAdapter.getItem(0))
    }

    @Test
    fun if_hasSelectHint_ShouldAddAnSelectHintAtTheLastPosition() {
        val expectedSelectHint = Subject("Select subject…", "")

        val subjectArrayAdapter =
            SubjectSpinnerAdapter.createWithAddHint(
                context,
                testSubjectList,
                true
            )

        assertEquals(expectedSelectHint, subjectArrayAdapter.getItem(subjectArrayAdapter.count))
    }

    @Test
    fun if_hasSelectHint_getCountShouldReturnSizeOfInsertedSubjects() {
        val subjectArrayAdapter =
            SubjectSpinnerAdapter.create(
                context,
                testSubjectList,
                true
            )

        assertTrue(subjectArrayAdapter.count == testSubjectList.size)
    }
}
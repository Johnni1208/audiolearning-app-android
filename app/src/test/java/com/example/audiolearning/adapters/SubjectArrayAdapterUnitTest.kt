package com.example.audiolearning.adapters

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.audiolearning.R
import com.example.audiolearning.data.db.entities.Subject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SubjectArrayAdapterUnitTest {
    private val context: Context = ApplicationProvider.getApplicationContext<Context>()
    private var testSubjectList = ArrayList<Subject>()
    private val textViewResourceId = R.layout.subject_spinner_item

    @Before
    fun setUp() {
        for (i in 1..10) {
            testSubjectList.add(Subject("test$i", "").apply {
                id = i.toLong()
            })
        }
    }

    @Test
    fun createWithAddHint_ShouldAddAnAddHintAtFirstPosition() {
        val expectedAddHintItem = Subject("Add new subject…", "")

        val subjectArrayAdapter =
            SubjectArrayAdapter.createWithAddHint(
                context,
                textViewResourceId,
                testSubjectList,
                false
            )

        assertEquals(expectedAddHintItem, subjectArrayAdapter.getItem(0))
    }

    @Test
    fun if_hasSelectHint_ShouldAddAnSelectHintAtTheLastPosition() {
        val expectedSelectHint = Subject("Select subject…", "")

        val subjectArrayAdapter =
            SubjectArrayAdapter.createWithAddHint(
                context,
                textViewResourceId,
                testSubjectList,
                true
            )

        assertEquals(expectedSelectHint, subjectArrayAdapter.getItem(subjectArrayAdapter.count))
    }

    @Test
    fun if_hasSelectHint_getCountShouldReturnSizeOfInsertedSubjects() {
        val subjectArrayAdapter =
            SubjectArrayAdapter.create(
                context,
                textViewResourceId,
                testSubjectList,
                true
            )
        assertTrue(subjectArrayAdapter.count == testSubjectList.size)
    }
}
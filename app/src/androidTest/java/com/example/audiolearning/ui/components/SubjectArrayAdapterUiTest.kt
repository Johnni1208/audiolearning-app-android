package com.example.audiolearning.ui.components

import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.audiolearning.R
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialog
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SubjectArrayAdapterUiTest {
    @Before
    fun setUp() {
        launchFragment<NewRecordingDialog>()
    }

    @Test
    fun onSpinnerClicked_ShouldOpenSpinner() {
        onView(withId(R.id.sp_select_subject))
            .check(matches(withSpinnerText("Select subject…")))
    }
}
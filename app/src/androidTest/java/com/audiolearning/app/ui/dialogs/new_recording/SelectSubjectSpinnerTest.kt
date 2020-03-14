package com.audiolearning.app.ui.dialogs.new_recording

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.audiolearning.app.R
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SelectSubjectSpinnerTest {
    private val addHintText = "Add new subject…"

    @Before
    fun setUp() {
        launchFragment<NewRecordingDialog>(Bundle().apply {
            putString(NewRecordingDialog.ARG_NEW_FILE_PATH, "testTempFile")
        })
    }

    @Test
    fun onNothingClicked_SpinnerShouldShowSelectSubjectHint() {
        onView(withId(R.id.sp_select_subject))
            .check(matches(withSpinnerText(containsString("Select subject…"))))
    }

    @Test
    fun onFirstItemClicked_ShouldShowTextOfAddHint() {
        onView(withId(R.id.sp_select_subject)).perform(click())
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())

        // Close the dialog
        onView(withId(R.id.btn_cancel_subject)).perform(click())

        onView(withId(R.id.sp_select_subject))
            .check(matches(withSpinnerText(containsString(addHintText))))
    }

    @Test
    fun onAddHintItemClicked_ShouldOpenCreateNewSubjectDialog() {
        onView(withId(R.id.sp_select_subject)).perform(click())
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())

        onView(withText("Create new subject")).check(matches(isDisplayed()))
    }
}
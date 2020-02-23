package com.example.audiolearning.ui.components

import android.os.Bundle
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.audiolearning.R
import com.example.audiolearning.ui.dialogs.new_recording.NewRecordingDialog
import org.hamcrest.Matchers.anything
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DoubleSelectSpinnerTest {
    @Before
    fun setUp() {
        launchFragment<NewRecordingDialog>(Bundle().apply {
            putString(NewRecordingDialog.newFilePathArgumentKey, "testTempFile")
        })
    }

    @Test
    fun onAddHintSelectedASecondTime_ShouldOpenDialogAgain() {
        // Click first time
        onView(withId(R.id.sp_select_subject)).perform(click())
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())

        onView(withId(R.id.btn_cancel_subject)).perform(click())

        // Click second time
        onView(withId(R.id.sp_select_subject)).perform(click())
        onData(anything()).atPosition(0).inRoot(isPlatformPopup()).perform(click())

        onView(withText("Create new subject")).check(matches(ViewMatchers.isDisplayed()))
    }
}
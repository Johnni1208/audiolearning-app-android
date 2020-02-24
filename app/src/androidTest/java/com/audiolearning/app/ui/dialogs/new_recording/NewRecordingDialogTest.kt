package com.audiolearning.app.ui.dialogs.new_recording

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.audiolearning.app.R
import com.audiolearning.app.ui.fragments.recorder.RecorderFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NewRecordingDialogTest {
    @Test
    fun stoppingTheRecording_ShouldOpenDialog() {
        FragmentScenario.launchInContainer(
            RecorderFragment::class.java,
            null,
            R.style.AppTheme,
            null
        )

        onView(withId(R.id.btn_record_and_stop)).perform(click())
        onView(withId(R.id.btn_record_and_stop)).perform(click())

        onView(withId(R.id.nr_toolbar)).inRoot(isDialog()).check(matches(isDisplayed()))
    }

    @Test
    fun clickingDiscard_ShouldCloseTheDialog() {
        launchFragment<NewRecordingDialog>(Bundle().apply {
            putString(NewRecordingDialog.newFilePathArgumentKey, "testTempFile")
        })

        onView(withId(R.id.btn_discard_recording)).perform(click())

        onView(withId(R.id.nr_toolbar)).check(doesNotExist())
    }
}
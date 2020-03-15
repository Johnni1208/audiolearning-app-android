package com.audiolearning.app.ui.dialogs.new_recording

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
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
    fun clickingDiscard_ShouldShowADialogAskingForConfirmation() {
        launchFragment<NewRecordingDialog>(Bundle().apply {
            putString(NewRecordingDialog.ARG_NEW_FILE_PATH, "testTempFile")
        })

        onView(withId(R.id.btn_discard_recording)).perform(click())

        onView(withText(R.string.drDialog_title)).check(matches(isDisplayed()))
    }
}
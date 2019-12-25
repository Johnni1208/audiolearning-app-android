package com.example.audiolearning.mainActivity

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.audiolearning.R
import com.example.audiolearning.fragments.recorder.RecorderFragment
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecorderButtonsUIChangeTest {

    @Before
    fun getScenario() {
        FragmentScenario.launchInContainer(RecorderFragment::class.java)
    }

    @Test
    fun testAreButtonsThere() {
        onView(withId(R.id.btn_record_and_stop))
            .check(matches(isDisplayed()))

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(isDisplayed()))
    }

    /* AudioRecorder IDLING */
    @Test
    fun testAudioRecorderIdling_RecordButtonHasTextRecord() {
        onView(withId(R.id.btn_record_and_stop))
            .check(matches(withText(R.string.record_text)))
    }

    @Test
    fun testAudioRecorderIdling_PauseButtonHasTextPause() {
        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(withText(R.string.pause_text)))
    }

    @Test
    fun testAudioRecorderIdling_PauseButtonIsNotClickable() {
        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(not(isEnabled())))

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(not(isClickable())))
    }

    /* AudioRecorder RECORDING */
    @Test
    fun testAudioRecorderRecording_RecordButtonHasTextStop() {
        startOrStop()

        onView(withId(R.id.btn_record_and_stop))
            .check(matches(withText(R.string.stop_text)))
    }

    @Test
    fun testAudioRecorderRecording_PauseButtonIsClickable() {
        startOrStop()

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(isEnabled()))
        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(isClickable()))
    }

    /* AudioRecorder PAUSING */
    @Test
    fun testAudioRecorderPausing_PauseButtonHasTextResume() {
        startOrStop()
        pauseOrResume()

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(withText(R.string.resume_text)))
    }

    @Test
    fun testAudioRecorderPausing_RecordButtonHasTextStop() {
        startOrStop()
        pauseOrResume()

        onView(withId(R.id.btn_record_and_stop))
            .check(matches(withText(R.string.stop_text)))
    }

    /* AudioRecorder RESUMING */
    @Test
    fun testAudioRecorderResuming_PauseButtonHasTextPause() {
        startOrStop()
        pauseOrResume()
        pauseOrResume()

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(withText(R.string.pause_text)))
    }

    /* AudioRecorder STOPPING */
    @Test
    fun testAudioRecorderStopping_RecordButtonHasTextRecord() {
        startOrStop()
        startOrStop()

        onView(withId(R.id.btn_record_and_stop))
            .check(matches(withText(R.string.record_text)))
    }

    @Test
    fun testAudioRecorderStopping_PauseButtonHasTextPause() {
        startOrStop()
        startOrStop()

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(withText(R.string.pause_text)))
    }

    @Test
    fun testAudioRecorderStopping_PauseButtonIsDisabled() {
        startOrStop()
        startOrStop()

        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(not(isEnabled())))
        onView(withId(R.id.btn_pause_and_resume))
            .check(matches(not(isClickable())))
    }

    /* Helper functions */
    private fun startOrStop() {
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())
    }

    private fun pauseOrResume() {
        onView(withId(R.id.btn_pause_and_resume))
            .perform(click())
    }
}
package com.example.audiolearning.fragments.recorder

import androidx.fragment.app.testing.FragmentScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.audiolearning.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

enum class TestTime(val value: Long, val string: String) {
    ZERO_SECONDS(0, "00:00:00"),
    ONE_SECOND(1000L, "00:00:01"),
}

@RunWith(AndroidJUnit4::class)
@LargeTest
class RecordTimerTest {
    // TODO: ALAE-10 Fix MediaRecorder Bug

    private fun assertTvTimerEqualTo(timeString: TestTime) {
        onView(withId(R.id.tv_record_time))
            .check(matches(withText(timeString.string)))
    }

    @Before
    fun getScenario() {
        FragmentScenario.launchInContainer(RecorderFragment::class.java)
    }

    @Test
    fun testOnRecordButtonClicked_TimerShouldStartCounting() {
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())
        runBlocking {
            delay(TestTime.ONE_SECOND.value)
        }

        assertTvTimerEqualTo(TestTime.ONE_SECOND)
    }

    @Test
    fun testOnStopButtonClicked_TimerShouldReset() {
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())
        runBlocking {
            delay(TestTime.ONE_SECOND.value)
        }
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())

        assertTvTimerEqualTo(TestTime.ZERO_SECONDS)
    }

    @Test
    fun testOnPauseButtonClicked_TimerShouldPause() {
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())
        runBlocking {
            delay(TestTime.ONE_SECOND.value)
        }
        onView(withId(R.id.btn_pause_and_resume))
            .perform(click())
        // We wait another second to be sure it really stopped
        runBlocking {
            delay(TestTime.ONE_SECOND.value)
        }

        assertTvTimerEqualTo(TestTime.ONE_SECOND)
    }

    @Test
    fun testOnResumeButtonClicked_TimerShouldResume() {
        onView(withId(R.id.btn_record_and_stop))
            .perform(click())
        onView(withId(R.id.btn_pause_and_resume))
            .perform(click())
            // Resume
            .perform(click())

        runBlocking {
            delay(TestTime.ONE_SECOND.value)
        }

        assertTvTimerEqualTo(TestTime.ONE_SECOND)
    }
}
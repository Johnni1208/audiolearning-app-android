package com.example.audiolearning.mainActivity


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.swipeRight
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.audiolearning.MainActivity
import com.example.audiolearning.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationBarTouchTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testNoSwipe_showsRecorder(){
        onView(withId(R.id.fragment_recorder))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_recorder)).check(matches(isSelected()))
    }

    @Test
    fun testSwipeRight_showsAboutUs(){
        onView(withId(R.id.pager))
            .perform(swipeRight())

        onView(withId(R.id.fragment_about_us))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_about_us)).check(matches(isSelected()))
    }

    @Test
    fun testSwipeLeft_showsSubjects(){
        onView(withId(R.id.pager))
            .perform(swipeLeft())

        onView(withId(R.id.fragment_subjects))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_subjects)).check(matches(isSelected()))
    }
}
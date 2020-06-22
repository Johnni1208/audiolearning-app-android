package com.audiolearning.app.ui.mainActivity.navigation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isSelected
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.audiolearning.app.R
import com.audiolearning.app.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class NavigationSwipeTest {
    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testClickNothing_showsRecorder(){
        onView(withId(R.id.fragment_recorder))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_recorder)).check(matches(isSelected()))
    }

    @Test
    fun testClickRecorderFromAboutUs_showsRecorder(){
        onView(withId(R.id.navigation_about_us))
            .perform(click())
        onView(withId(R.id.navigation_recorder))
            .perform(click())

        onView(withId(R.id.fragment_recorder))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_recorder)).check(matches(isSelected()))
    }

    @Test
    fun testClickAboutUs_showsAboutUs(){
        onView(withId(R.id.navigation_about_us))
            .perform(click())

        onView(withId(R.id.fragment_about_us))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_about_us)).check(matches(isSelected()))
    }

    @Test
    fun testClickSubjects_showsSubjects(){
        onView(withId(R.id.navigation_subjects))
            .perform(click())

        onView(withId(R.id.navigation_subjects))
            .check(matches(isDisplayed()))
        onView(withId(R.id.navigation_subjects)).check(matches(isSelected()))
    }
}
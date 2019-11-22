package com.example.audiolearning.mainActivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
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
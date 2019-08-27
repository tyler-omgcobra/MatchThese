package org.omgcobra.matchthese.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.omgcobra.matchthese.MainActivity
import org.omgcobra.matchthese.R
import java.util.*

@RunWith(AndroidJUnit4::class)
@LargeTest
class ItemBehaviorTest {

    private lateinit var name: String
    private lateinit var tag: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        name = UUID.randomUUID().toString()
        tag = UUID.randomUUID().toString()
    }

    @Test
    fun addItem_sameActivity() {
        onView(withId(R.id.floating_action_button_item))
                .perform(click())
        onView(withId(R.id.item_edit_name))
                .check(matches(isDisplayed()))
                .perform(typeText(name))
        onView(withId(R.id.item_edit_tags))
                .check(matches(isDisplayed()))
                .perform(typeText(tag))
        onView(withId(R.id.action_save))
                .check(matches(isDisplayed()))
                .perform(click())

        onView(withText(name))
                .check(matches(isDisplayed()))
                .perform(swipeLeft())
        onView(withText(name))
                .check(doesNotExist())

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.open())
                .check(matches(DrawerMatchers.isOpen()))

        onView(withText(R.string.tags_title))
                .perform(click())
        onView(withText(tag))
                .check(matches(isDisplayed())).check(matches(isDisplayed()))
                .perform(swipeLeft())
        onView(withText(tag))
                .check(doesNotExist())
    }
}
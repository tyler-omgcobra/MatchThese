package org.omgcobra.matchthese.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
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

    private lateinit var stringToBeTyped: String

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun initValidString() {
        stringToBeTyped = UUID.randomUUID().toString()
    }

    @Test
    fun addItem_sameActivity() {
        onView(withId(R.id.floating_action_button_item))
                .perform(click())
        onView(withId(R.id.item_edit_name))
                .check(matches(isDisplayed()))
                .perform(typeText(stringToBeTyped))
        onView(withText(R.string.add_btn_text))
                .check(matches(isDisplayed()))
                .perform(click())
        onView(withText(stringToBeTyped))
                .check(matches(isDisplayed()))
                .perform(swipeLeft())
                .check(doesNotExist())

    }
}